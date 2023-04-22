package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.cms.LearningResultDetailRequestDto;
import com.kma.project.chatapp.dto.response.cms.LearningResultDetailResponseDto;
import com.kma.project.chatapp.service.LearningResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/learning-result")
@Api(tags = "Quản lí điểm")
public class LearningResultController {

    @Autowired
    private LearningResultService service;

    @ApiOperation(value = "Lấy danh sách điểm các môn học")
    @GetMapping
    public List<LearningResultDetailResponseDto> getAllClass(Long studentId, String year, Integer term) {
        return service.getAllResult(studentId, year, term);
    }

    @ApiOperation(value = "Cập nhật điểm")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody LearningResultDetailRequestDto request, @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.updateScore(id, request));
    }

    @ApiOperation(value = "Tính điểm tổng kết")
    @PostMapping
    public void calculateFinalScore(Long studentId, String semesterYear) {
        service.calculateFinalScore(studentId, semesterYear);
    }

}
