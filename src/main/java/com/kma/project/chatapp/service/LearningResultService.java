package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.cms.LearningResultDetailRequestDto;
import com.kma.project.chatapp.dto.response.cms.LearningResultDetailResponseDto;

import java.util.List;

public interface LearningResultService {

    LearningResultDetailResponseDto updateScore(Long id, LearningResultDetailRequestDto dto);

    List<LearningResultDetailResponseDto> getAllResult(Long studentId, String year, Integer term);

    void calculateFinalScore(Long studentId, String semesterYear);
}
