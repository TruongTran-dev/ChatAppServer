package com.kma.project.chatapp.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInputDto {

    @NotBlank(message = "{error.username-not-null}")
    @Size(min = 3, max = 20, message = "{error.username-not-valid}")
    private String username;

    @NotBlank(message = "{error.email-not-null}")
    @Email(message = "{error.email-not-valid}")
    private String email;

    @NotBlank(message = "{error.password-not-null}")
    @Size(min = 6, max = 40, message = "{error.password-not-valid}")
    private String password;

    @Size(min = 6, max = 40, message = "{error.password-not-valid}")
    private String confirmPassword;

    private List<String> roles;

    private String fullName;

    private String phone;

    private Boolean isFillProfileKey;

}
