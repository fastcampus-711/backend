package com.aptner.v3.attach.service.Impl;

import com.aptner.v3.attach.service.S3Service;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("dev")
public class DBServiceImpl implements S3Service {
    @Override
    public String getFileUrl(String key) {
        return null;
    }

    @Override
    public String uploadFile(String key, MultipartFile multipartFile) {
        return null;
    }

    @Override
    public byte[] downloadFile(String key) {
        return new byte[0];
    }

    @Override
    public void deleteFile(String key) {

    }
}
