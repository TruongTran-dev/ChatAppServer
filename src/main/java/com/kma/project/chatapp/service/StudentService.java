package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.cms.StudentRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;

public interface StudentService {

    StudentResponseDto add(StudentRequestDto dto);

    StudentResponseDto update(Long id, StudentRequestDto dto);

    void delete(Long id);

    PageResponse<StudentResponseDto> getAllStudent(Integer page, Integer size, String sort, String search, Long classId);
}