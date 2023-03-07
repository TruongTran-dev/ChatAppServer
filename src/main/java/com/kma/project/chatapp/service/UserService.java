package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.*;
import com.kma.project.chatapp.exception.AppResponseDto;

public interface UserService {

    AppResponseDto signUp(SignUpRequest signupRequest);

    AppResponseDto signIn(LoginRequest loginRequest);

    void verifyOtp(OtpRequestDto otpRequestDto);

    void createNewPassword(NewPasswordRequestDto newPasswordRequestDto);

    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);

}
