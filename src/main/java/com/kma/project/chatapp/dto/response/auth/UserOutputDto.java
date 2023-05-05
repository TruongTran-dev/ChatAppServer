package com.kma.project.chatapp.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOutputDto {

    private Long id;

    private String username;

    private String email;

    private Set<RoleOutputDto> roles;

    private String fullName;

    private String phone;

    private Boolean isFillProfileKey;

    private String fileUrl;

    private List<StudentResponseDto> students;

}
