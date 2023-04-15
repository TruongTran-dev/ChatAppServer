package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.cms.SubjectRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.SubjectResponseDto;
import com.kma.project.chatapp.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/subjects")
@Api(tags = "Quản lí môn học")
public class SubjectController {

    @Autowired
    private SubjectService service;

    @ApiOperation(value = "Thêm mới môn học")
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody SubjectRequestDto request) {
        return ResponseEntity.ok(service.add(request));
    }

    @ApiOperation(value = "Lấy danh sách môn học")
    @GetMapping
    public PageResponse<SubjectResponseDto> getAllUser(Integer page, Integer size, String sort, String search) {
        return service.getAllSubject(page, size, sort, search);
    }

    @ApiOperation(value = "Cập nhật môn học")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody SubjectRequestDto request, @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @ApiOperation(value = "Xóa môn học")
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        service.delete(id);
    }

}
