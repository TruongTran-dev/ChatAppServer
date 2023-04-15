package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.cms.NewRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.NewResponseDto;
import com.kma.project.chatapp.service.NewService;
import com.kma.project.chatapp.service.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/news")
@Api(tags = "Quản lí bài viết")
public class NewController {

    @Autowired
    NewService newService;

    @Autowired
    UploadFileService uploadFileService;

    @ApiOperation(value = "Thêm mới bài viết")
    @PostMapping
    public ResponseEntity<?> addNew(@Valid @RequestBody NewRequestDto request) {
        return ResponseEntity.ok(newService.add(request));
    }

    @ApiOperation(value = "Lấy danh sách bài viết")
    @GetMapping
    public PageResponse<NewResponseDto> getAllUser(Integer page, Integer size, String sort, String search) {
        return newService.getAllNew(page, size, sort, search);
    }

    @ApiOperation(value = "Cập nhật bài viết")
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody NewRequestDto request, @PathVariable("id") Long id) {
        return ResponseEntity.ok(newService.update(id, request));
    }

    @ApiOperation(value = "Xóa bài viết")
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        newService.delete(id);
    }

    @ApiOperation(value = "Lấy chi tiết bài viết")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newService.getDetail(id));
    }

    @ApiOperation(value = "Thêm mới ảnh")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/upload")
    public String add(@RequestPart("file") MultipartFile file) {
        return uploadFileService.uploadFileCloud(file);
    }


}
