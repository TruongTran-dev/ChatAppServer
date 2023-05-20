package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.SubjectRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.SubjectResponseDto;
import com.kma.project.chatapp.entity.SubjectEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.SubjectMapper;
import com.kma.project.chatapp.repository.SubjectRepositoy;
import com.kma.project.chatapp.service.SubjectService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Transactional(readOnly = true)
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepositoy repositoy;

    @Autowired
    SubjectMapper mapper;

    @Transactional
    @Override
    public SubjectResponseDto add(SubjectRequestDto dto) {
        SubjectEntity entity = mapper.convertToEntity(dto);
        repositoy.save(entity);
        entity.setCode("SUBID" + "00" + entity.getId());
        return mapper.convertToDto(entity);
    }

    @Transactional
    @Override
    public SubjectResponseDto update(Long id, SubjectRequestDto dto) {
        SubjectEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.subject-not-found")).build());
        mapper.update(dto, entity);
        return mapper.convertToDto(repositoy.save(entity));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        SubjectEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.subject-not-found")).build());
        repositoy.delete(entity);
    }

    @Transactional
    @Override
    public PageResponse<SubjectResponseDto> getAllSubject(Integer page, Integer size, String sort, String search) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);
        Page<SubjectEntity> pageEntity = repositoy.findAllByNameLikeIgnoreCase(pageable, PageUtils.buildSearch(search));
        return PageUtils.formatPageResponse(pageEntity.map(entity -> mapper.convertToDto(entity)));
    }
}
