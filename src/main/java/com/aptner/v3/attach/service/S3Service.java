package com.aptner.v3.attach.service;

import com.aptner.v3.global.exception.AttachException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

import static com.aptner.v3.global.error.ErrorCode.S3_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket:aptners}")
    private String bucket;

    public String uploadFile(String key, MultipartFile multipartFile) {
        upload(key, multipartFile);
        return getFileUrl(key);
    }

    private void upload(String key, MultipartFile multipartFile) {

//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("x-amz-meta-myVal", "test");
        log.info("bucket name: {}", bucket);
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(multipartFile.getContentType())
//                    .metadata(metadata)
//                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
            s3Client.putObject(objectRequest, requestBody);
        } catch (IOException e) {
            log.debug("s3 uploading problem.");
        }
    }

    public void deleteFile(String key) {
        delete(key);
    }

    private void delete(String key) {
        try {
            if (existsFile(key)) {
                s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
            }
        } catch (S3Exception e) {
            throw new AttachException(S3_ERROR);
        }
    }

    private boolean existsFile(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public String getFileUrl(String key) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build()).toString();
    }

    public byte[] getObjectBytes(String key) {
        byte[] data = null;
        try {
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder().key(key).bucket(bucket).build());
            data = objectBytes.asByteArray();

        } catch (S3Exception e) {
            throw new AttachException(S3_ERROR);
        }
        return data;
    }
}
