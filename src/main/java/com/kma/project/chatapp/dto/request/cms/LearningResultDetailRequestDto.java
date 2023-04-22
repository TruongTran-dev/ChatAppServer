package com.kma.project.chatapp.dto.request.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningResultDetailRequestDto {

//    private Long studentId;
//
//    private Long subjectId;
//
//    private Long semesterId; // học kì

    private Float oralTestScore; // điểm kiểm tra miệng

    private Float m15TestScore; // điểm kiểm tra 15 phut

    private Float m45TestScore; // điểm kiểm tra 45 phut

    private Float semesterTestScore; // điểm kiểm tra học kì

//    private Float semesterSummaryScore; // điểm trung bình môn của 1 học kì

}
