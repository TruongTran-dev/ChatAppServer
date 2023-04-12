package com.kma.project.chatapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    String uploadFileCloud(MultipartFile file);
}
