package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.cms.ClassRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.ClassResponseDto;

public interface ClassService {

    ClassResponseDto add(ClassRequestDto dto);

    ClassResponseDto update(Long id, ClassRequestDto dto);

    ClassResponseDto getDetail(Long id);

    void delete(Long id);

    PageResponse<ClassResponseDto> getAllClass(Integer page, Integer size, String sort, String search);
}
