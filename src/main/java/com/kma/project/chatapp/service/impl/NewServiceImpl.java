package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.NewRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.NewResponseDto;
import com.kma.project.chatapp.entity.NewEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.NewMapper;
import com.kma.project.chatapp.repository.NewRepository;
import com.kma.project.chatapp.service.NewService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Transactional(readOnly = true)
@Service
public class NewServiceImpl implements NewService {

    @Autowired
    NewRepository repository;

    @Autowired
    NewMapper mapper;

    @Transactional
    @Override
    public NewResponseDto add(NewRequestDto dto) {
        NewEntity entity = mapper.convertToEntity(dto);
        repository.save(entity);
        return mapper.convertToDto(entity);
    }

    @Transactional
    @Override
    public NewResponseDto update(Long id, NewRequestDto dto) {
        NewEntity newEntity = repository.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.new-not-found")).build());
        newEntity = mapper.update(dto, newEntity);
        repository.save(newEntity);
        return mapper.convertToDto(newEntity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        NewEntity newEntity = repository.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.new-not-found")).build());
        repository.delete(newEntity);
    }

    @Override
    public NewResponseDto getDetail(Long id) {
        NewEntity newEntity = repository.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.new-not-found")).build());
        return mapper.convertToDto(newEntity);
    }

    @Override
    public PageResponse<NewResponseDto> getAllNew(Integer page, Integer size, String sort, String search) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);
        Page<NewEntity> pageEntity = repository.findAllByTitleLikeIgnoreCase(pageable, PageUtils.buildSearch(search));
        return PageUtils.formatPageResponse(pageEntity.map(entity -> mapper.convertToDto(entity)));
    }
}
