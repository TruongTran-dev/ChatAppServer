package com.kma.project.chatapp.dto.response.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentResponseDto {

    private Long id;

    private String name;

    private String code;

    private LocalDate dateOfBirth;

    private String imageUrl;

    private ClassResponseDto classResponse;

    private String className;

    private Float mediumScore; // điểm trung bình môn cả năm

    private Float hk1SubjectMediumScore; // điểm trung bình các môn học kì 1

    private Float hk2SubjectMediumScore; // điểm trung bình các môn học kì 2

}
