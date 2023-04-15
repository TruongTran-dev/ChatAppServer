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

    private LocalDate dateOfBirth;

    private String imageUrl;

    private ClassResponseDto classResponse;

}
