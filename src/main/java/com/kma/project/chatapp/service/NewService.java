package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.NewRequestDto;
import com.kma.project.chatapp.dto.response.NewResponseDto;
import com.kma.project.chatapp.dto.response.PageResponse;

public interface NewService {

    NewResponseDto add(NewRequestDto dto);

    NewResponseDto update(Long id, NewRequestDto dto);

    void delete(Long id);

    NewResponseDto getDetail(Long id);

    PageResponse<NewResponseDto> getAllNew(Integer page, Integer size, String sort, String search);

}
