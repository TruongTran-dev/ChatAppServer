package com.kma.project.chatapp.dto.response.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningResultDetailResponseDto {

    private Long id;

    private Long subjectId;

    private String subjectName;

    private Long learningResultId;

    private Float oralTestScore; // điểm kiểm tra miệng

    private Float m15TestScore; // điểm kiểm tra 15 phut

    private Float m45TestScore; // điểm kiểm tra 45 phut

    private Float semesterTestScore; // điểm kiểm tra học kì

    private Float semesterSummaryScore; // điểm trung bình môn của 1 học kì


}
