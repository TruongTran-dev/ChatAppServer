package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.*;
import com.kma.project.chatapp.dto.response.JwtResponse;

public interface UserService {

    void signUp(SignUpRequest signupRequest);

    JwtResponse signIn(LoginRequest loginRequest);

    void verifyOtp(OtpRequestDto otpRequestDto);

    void createNewPassword(NewPasswordRequestDto newPasswordRequestDto);

    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);

}
