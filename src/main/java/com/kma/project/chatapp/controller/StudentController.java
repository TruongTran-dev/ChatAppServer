package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.cms.StudentRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.StudentLearningResultResponseDto;
import com.kma.project.chatapp.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/students")
@Api(tags = "Quản lí học sinh")
public class StudentController {

    @Autowired
    private StudentService service;

    @ApiOperation(value = "Thêm mới học sinh")
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody StudentRequestDto request) {
        return ResponseEntity.ok(service.add(request));
    }

    @ApiOperation(value = "Lấy danh sách học sinh")
    @GetMapping
    public PageResponse<StudentLearningResultResponseDto> getAllStudent(Integer page, Integer size, String sort,
                                                                        String search, String semesterYear,
                                                                        @RequestParam(required = false) Long classId) {
        return service.getAllStudent(page, size, sort, search, semesterYear, classId);
    }

    @ApiOperation(value = "Cập nhật học sinh")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody StudentRequestDto request, @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @ApiOperation(value = "Xóa học sinh")
    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @ApiOperation(value = "Lấy chi tiết học sinh")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailStudent(@PathVariable("id") Long id, String semesterYear) {
        return ResponseEntity.ok(service.getDetail(id, semesterYear));
    }

    @ApiOperation(value = "Tìm học sinh bằng SSID")
    @GetMapping("/detail")
    public ResponseEntity<?> findByCode(@RequestParam String code) {
        return ResponseEntity.ok(service.getDetailByCode(code));
    }

}
