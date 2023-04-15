package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.StudentRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;
import com.kma.project.chatapp.entity.ClassEntity;
import com.kma.project.chatapp.entity.StudentEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.ClassMapper;
import com.kma.project.chatapp.mapper.StudentMapper;
import com.kma.project.chatapp.repository.ClassRepository;
import com.kma.project.chatapp.repository.StudentRepository;
import com.kma.project.chatapp.service.StudentService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository repositoy;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    StudentMapper mapper;

    @Autowired
    ClassMapper classMapper;

    @Transactional
    @Override
    public StudentResponseDto add(StudentRequestDto dto) {
        ClassEntity classEntity = classRepository.findById(dto.getClassId())
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        StudentEntity entity = mapper.convertToEntity(dto);
        entity.setClassEntity(classEntity);
        return mapper.convertToDto(repositoy.save(entity));
    }

    @Transactional
    @Override
    public StudentResponseDto update(Long id, StudentRequestDto dto) {
        StudentEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.student-not-found")).build());
        if (!Objects.equals(entity.getClassEntity().getId(), dto.getClassId())) {
            ClassEntity classEntity = classRepository.findById(dto.getClassId())
                    .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
            entity.setClassEntity(classEntity);
        }
        mapper.update(dto, entity);
        return mapper.convertToDto(repositoy.save(entity));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        StudentEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.student-not-found")).build());
        repositoy.delete(entity);
    }

    @Transactional
    @Override
    public PageResponse<StudentResponseDto> getAllStudent(Integer page, Integer size, String sort, String search, Long classId) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());

        Page<StudentEntity> pageEntity = repositoy.
                findAllByNameLikeIgnoreCaseAndClassEntity(pageable, PageUtils.buildSearch(search), classEntity);

        return PageUtils.formatPageResponse(pageEntity.map(entity -> {
            StudentResponseDto responseDto = mapper.convertToDto(entity);
            responseDto.setClassResponse(classMapper.convertToDto(classEntity));
            return responseDto;
        }));
    }
}
