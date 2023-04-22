package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.cms.ClassRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.ClassResponseDto;
import com.kma.project.chatapp.service.ClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/classes")
@Api(tags = "Quản lí lớp học")
public class ClassController {

    @Autowired
    private ClassService service;

    @ApiOperation(value = "Thêm mới lớp học")
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody ClassRequestDto request) {
        return ResponseEntity.ok(service.add(request));
    }

    @ApiOperation(value = "Lấy danh sách lớp học")
    @GetMapping
    public PageResponse<ClassResponseDto> getAllClass(Integer page, Integer size, String sort, String search) {
        return service.getAllClass(page, size, sort, search);
    }

    @ApiOperation(value = "Cập nhật lớp học")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ClassRequestDto request, @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @ApiOperation(value = "Lấy chi tiết lớp học")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getDetail(id));
    }

    @ApiOperation(value = "Xóa lớp học")
    @DeleteMapping("{id}")
    public void deleteClass(@PathVariable("id") Long id) {
        service.delete(id);
    }

}
