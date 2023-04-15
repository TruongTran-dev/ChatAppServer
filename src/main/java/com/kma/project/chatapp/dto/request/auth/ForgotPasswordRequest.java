package com.kma.project.chatapp.dto.request.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "{error.email-not-null}")
    private String email;

}
