package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.ClassRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.ClassResponseDto;
import com.kma.project.chatapp.entity.ClassEntity;
import com.kma.project.chatapp.entity.ClassSubjectMapEntity;
import com.kma.project.chatapp.entity.SubjectEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.ClassMapper;
import com.kma.project.chatapp.mapper.SubjectMapper;
import com.kma.project.chatapp.repository.ClassRepository;
import com.kma.project.chatapp.repository.ClassSubjectMapRepository;
import com.kma.project.chatapp.repository.SubjectRepositoy;
import com.kma.project.chatapp.service.ClassService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassRepository repositoy;

    @Autowired
    SubjectRepositoy subjectRepositoy;

    @Autowired
    ClassSubjectMapRepository classSubjectMapRepository;

    @Autowired
    ClassMapper mapper;

    @Autowired
    SubjectMapper subjectMapper;

    @Transactional
    @Override
    public ClassResponseDto add(ClassRequestDto dto) {
        ClassEntity entity = mapper.convertToEntity(dto);
        repositoy.save(entity);
        entity.setCode("CLAID" + "00" + entity.getId());

        saveSubjectClassMap(dto, entity);
        return getDetail(entity.getId());
    }

    public void saveSubjectClassMap(ClassRequestDto dto, ClassEntity entity) {
        List<SubjectEntity> subjectEntityList = subjectRepositoy.findAllById(dto.getSubjectIds());
        Map<Long, SubjectEntity> mapSubject = subjectEntityList.stream().collect(Collectors.toMap(SubjectEntity::getId, Function.identity()));

        List<ClassSubjectMapEntity> classSubjectMapEntities = new ArrayList<>();
        dto.getSubjectIds().forEach(aLong -> {
            SubjectEntity subjectEntity = mapSubject.get(aLong);
            if (subjectEntity == null) {
                throw AppException.builder().errorCodes(Collections.singletonList("error.subject-not-found")).build();
            }
            ClassSubjectMapEntity classSubjectMapEntity = new ClassSubjectMapEntity();
            classSubjectMapEntity.setClassId(entity.getId());
            classSubjectMapEntity.setSubjectId(aLong);
            classSubjectMapEntities.add(classSubjectMapEntity);
        });
        classSubjectMapRepository.saveAll(classSubjectMapEntities);
    }

    @Transactional
    @Override
    public ClassResponseDto update(Long id, ClassRequestDto dto) {
        ClassEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        mapper.update(dto, entity);
        List<ClassSubjectMapEntity> classSubjectMapEntities = classSubjectMapRepository.findAllByClassId(id);
        classSubjectMapRepository.deleteAll(classSubjectMapEntities);
        saveSubjectClassMap(dto, entity);
        repositoy.save(entity);
        return getDetail(id);
    }

    @Override
    public ClassResponseDto getDetail(Long id) {
        ClassEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        List<ClassSubjectMapEntity> classSubjectMapEntities = classSubjectMapRepository.findAllByClassId(id);

        List<Long> subjectIds = new ArrayList<>();
        classSubjectMapEntities.forEach(classSubjectMapEntity -> {
            subjectIds.add(classSubjectMapEntity.getSubjectId());
        });

        List<SubjectEntity> subjectEntityList = subjectRepositoy.findAllById(subjectIds);

        ClassResponseDto classResponseDto = mapper.convertToDto(entity);
        classResponseDto.setSubjectDatas(subjectEntityList.stream().map(entity1 -> subjectMapper.convertToDto(entity1)).collect(Collectors.toList()));

        return classResponseDto;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ClassEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        repositoy.delete(entity);
    }

    public ClassResponseDto mapToDto(ClassEntity entity) {
        List<ClassSubjectMapEntity> classSubjectMapEntities = classSubjectMapRepository.findAllByClassId(entity.getId());

        List<Long> subjectIds = new ArrayList<>();
        classSubjectMapEntities.forEach(classSubjectMapEntity -> {
            subjectIds.add(classSubjectMapEntity.getSubjectId());
        });

        List<SubjectEntity> subjectEntityList = subjectRepositoy.findAllById(subjectIds);

        ClassResponseDto classResponseDto = mapper.convertToDto(entity);
        classResponseDto.setSubjectDatas(subjectEntityList.stream().map(entity1 -> subjectMapper.convertToDto(entity1)).collect(Collectors.toList()));

        return classResponseDto;
    }

    @Transactional
    @Override
    public PageResponse<ClassResponseDto> getAllClass(Integer page, Integer size, String sort, String search) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);
        Page<ClassEntity> pageEntity = repositoy.findAllByNameLikeIgnoreCase(pageable, PageUtils.buildSearch(search));
        return PageUtils.formatPageResponse(pageEntity.map(this::mapToDto));
    }
}
