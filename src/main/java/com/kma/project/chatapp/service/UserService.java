package com.kma.project.chatapp.service;

import com.kma.project.chatapp.dto.request.*;
import com.kma.project.chatapp.dto.response.PageResponse;
import com.kma.project.chatapp.dto.response.UserOutputDto;
import com.kma.project.chatapp.exception.AppResponseDto;

public interface UserService {

    AppResponseDto signUp(UserInputDto inputDto);

    AppResponseDto signIn(LoginRequest loginRequest);

    void verifyOtp(OtpRequestDto otpRequestDto);

    void createNewPassword(NewPasswordRequestDto newPasswordRequestDto);

    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    PageResponse<UserOutputDto> getAllUser(Integer page, Integer size, String sort, String search);

    UserOutputDto updateUser(Long userId, UserInputDto dto);

    void delete(Long userId);

    UserOutputDto getDetailUser(Long userId);

}
