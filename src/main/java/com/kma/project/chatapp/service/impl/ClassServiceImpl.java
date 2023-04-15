package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.ClassRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.ClassResponseDto;
import com.kma.project.chatapp.entity.ClassEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.ClassMapper;
import com.kma.project.chatapp.repository.ClassRepository;
import com.kma.project.chatapp.service.ClassService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Transactional(readOnly = true)
@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassRepository repositoy;

    @Autowired
    ClassMapper mapper;

    @Transactional
    @Override
    public ClassResponseDto add(ClassRequestDto dto) {
        ClassEntity entity = mapper.convertToEntity(dto);
        return mapper.convertToDto(repositoy.save(entity));
    }

    @Transactional
    @Override
    public ClassResponseDto update(Long id, ClassRequestDto dto) {
        ClassEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        mapper.update(dto, entity);
        return mapper.convertToDto(repositoy.save(entity));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ClassEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        repositoy.delete(entity);
    }

    @Transactional
    @Override
    public PageResponse<ClassResponseDto> getAllClass(Integer page, Integer size, String sort, String search) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);
        Page<ClassEntity> pageEntity = repositoy.findAllByNameLikeIgnoreCase(pageable, PageUtils.buildSearch(search));
        return PageUtils.formatPageResponse(pageEntity.map(entity -> mapper.convertToDto(entity)));
    }
}
