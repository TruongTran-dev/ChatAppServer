package com.kma.project.chatapp.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "{error.username-not-null}")
    private String username;

    @NotBlank(message = "{error.password-not-null}")
    private String password;
}
