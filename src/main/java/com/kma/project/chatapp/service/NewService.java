package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.cms.NewRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.NewResponseDto;

public interface NewService {

    NewResponseDto add(NewRequestDto dto);

    NewResponseDto update(Long id, NewRequestDto dto);

    void delete(Long id);

    NewResponseDto getDetail(Long id);

    PageResponse<NewResponseDto> getAllNew(Integer page, Integer size, String sort, String search);

}
