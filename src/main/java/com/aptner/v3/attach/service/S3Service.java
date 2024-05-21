package com.aptner.v3.attach.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String getFileUrl(String key);

    String uploadFile(String key, MultipartFile multipartFile);

    byte[] downloadFile(String key);

    void deleteFile(String key);
}
