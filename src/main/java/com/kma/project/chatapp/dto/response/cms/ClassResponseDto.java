package com.kma.project.chatapp.dto.response.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassResponseDto {

    private Long id;

    private String code;

    private String name;

    private String year;

    private List<SubjectResponseDto> subjectDatas;

}
