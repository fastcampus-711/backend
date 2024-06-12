package com.aptner.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Component
public class ResizeRunner implements CommandLineRunner {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.accessKey:default}")
    private String accessKey;

    @Value("${aws.s3.secretKey:default}")
    private String secretKey;

    @Override
    public void run(String... args) throws Exception {

        Region region = Region.AP_SOUTHEAST_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                .build();

        String bucketName = bucket;
        String directory = "file/";

//        ListObjectsRequest listObjects = ListObjectsRequest
//                .builder()
//                .prefix(directory)
//                .bucket(bucketName)
//                .build();
//        ListObjectsResponse res = s3.listObjects(listObjects);
//        java.util.List<S3Object> contents = res.contents();
//        for (S3Object myValue : contents) {
//            System.out.print("\n The name of the key is " + myValue.key());
//            System.out.print("\n The object is " + myValue.size() + " KBs");
//            System.out.print("\n The owner is " + myValue.owner());
//        }

        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(directory)
                .delimiter("/")
                .build();

        ListObjectsV2Response listObjectsV2Response;
        do {
            listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);

            List<S3Object> objectSummaries = listObjectsV2Response.contents();
            for (S3Object s3Object : objectSummaries) {
                String key = s3Object.key();
                log.debug(key);
                if (isImageFile(key)) {
                    // S3에서 이미지 다운로드
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    ResponseBytes<GetObjectResponse> s3ObjectBytes = s3.getObjectAsBytes(getObjectRequest);

                    // 이미지 리사이즈
                    MultipartFile resizedImage = resizeImage(s3ObjectBytes.asByteArray(), key, 600);

                    // 새로운 키 생성
                    String newKey = key.substring(0, key.lastIndexOf(".")) + "/600" + key.substring(key.lastIndexOf("."));

                    // 리사이즈된 이미지 업로드
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(newKey)
                            .build();

                    s3.putObject(putObjectRequest, RequestBody.fromBytes(resizedImage.getBytes()));
                }
            }

            listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(directory)
                    .delimiter("/")
                    .continuationToken(listObjectsV2Response.nextContinuationToken())
                    .build();

        } while (listObjectsV2Response.isTruncated());
    }

    private boolean isImageFile(String key) {
        String lowerCaseKey = key.toLowerCase();
        return lowerCaseKey.endsWith(".jpeg") || lowerCaseKey.endsWith(".jpg") || lowerCaseKey.endsWith(".png");
    }

    private MultipartFile resizeImage(byte[] imageBytes, String key, int targetWidth) throws IOException {
        // MultipartFile을 BufferedImage로 변환
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // 원본 이미지의 비율 계산
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int targetHeight = (int) ((double) targetWidth / originalWidth * originalHeight);

        // 이미지 리사이즈
        Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        // 리사이즈된 이미지를 BufferedImage로 변환
        BufferedImage bufferedResizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedResizedImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();

        // BufferedImage를 ByteArrayOutputStream으로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String extension = getExtensionFromKey(key);
        ImageIO.write(bufferedResizedImage, extension, baos);
        byte[] resizedBytes = baos.toByteArray();

        // 리사이즈된 이미지를 MultipartFile로 변환
        return new MultipartFile() {
            @Override
            public String getName() {
                return key;
            }

            @Override
            public String getOriginalFilename() {
                return key.substring(key.lastIndexOf("/") + 1);
            }

            @Override
            public String getContentType() {
                return "image/" + extension;
            }

            @Override
            public boolean isEmpty() {
                return resizedBytes.length == 0;
            }

            @Override
            public long getSize() {
                return resizedBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return resizedBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(resizedBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.write(dest.toPath(), resizedBytes);
            }
        };
    }

    private String getExtensionFromKey(String key) {
        return key.substring(key.lastIndexOf(".") + 1).toLowerCase();
    }
}
