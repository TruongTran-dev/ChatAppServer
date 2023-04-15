package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.auth.UserUpdateDto;
import com.kma.project.chatapp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "Quản lí người dùng App")
public class UserAppController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "Cập nhật tài khoản")
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto request, @PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @ApiOperation(value = "Lấy chi tiết tài khoản")
    @GetMapping("{id}")
    public ResponseEntity<?> getDetailUser(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.getDetailUser(userId));
    }

}
