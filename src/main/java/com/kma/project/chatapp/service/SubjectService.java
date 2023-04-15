package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.cms.SubjectRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.SubjectResponseDto;

public interface SubjectService {

    SubjectResponseDto add(SubjectRequestDto dto);

    SubjectResponseDto update(Long id, SubjectRequestDto dto);

    void delete(Long id);

    PageResponse<SubjectResponseDto> getAllSubject(Integer page, Integer size, String sort, String search);
}
