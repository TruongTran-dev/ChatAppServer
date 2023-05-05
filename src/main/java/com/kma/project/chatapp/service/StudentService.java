package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.cms.StudentRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.StudentLearningResultResponseDto;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;

public interface StudentService {

    StudentResponseDto add(StudentRequestDto dto);

    StudentResponseDto update(Long id, StudentRequestDto dto);

    StudentResponseDto getDetail(Long id, String semesterYear);

    StudentResponseDto getDetailByCode(String code);


    void delete(Long id);

    PageResponse<StudentLearningResultResponseDto> getAllStudent(Integer page, Integer size, String sort, String search
            , String semesterYear, Long classId);
}
