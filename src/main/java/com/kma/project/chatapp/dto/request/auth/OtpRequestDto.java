package com.kma.project.chatapp.dto.request.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OtpRequestDto {

    @NotBlank(message = "{error.otp-not-null}")
    private String otp;

    @NotBlank(message = "{error.email-not-null}")
    private String email;

}
