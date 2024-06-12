package com.aptner.v3.attach.service;

import com.aptner.v3.attach.Attach;
import com.aptner.v3.attach.AttachType;
import com.aptner.v3.attach.repository.AttachRepository;
import com.aptner.v3.global.exception.AttachException;
import com.aptner.v3.global.util.MultipartUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;
import static com.aptner.v3.global.util.MultipartUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachService {

    private final AttachRepository attachRepository;
    private final S3Service s3Service;

    public List<Attach> uploads(AttachType type, List<MultipartFile> files) {
        List<Attach> list = new ArrayList<>();
        for (MultipartFile file : files) {
            if (MultipartUtil.isFileValid(type, file)) {
                Attach attach = upload(type, file);
                list.add(attach);
            }
        }
        return list;
    }

    public Attach upload(AttachType type, MultipartFile file) {

        String uuid = createFileId();
        String key = createKey(type.getLocation(), uuid, file.getContentType());

        // 원본 데이터
        MultipartFile resizedFile = resizeImage(file, 1200); // 원하는 크기로 조정
        String url = s3Service.uploadFile(key, resizedFile);

        // ThumbNail 데이터 ( 나눔 장터 )
        resizedFile = resizeImage(file, 300); // 원하는 크기로 조정
        String thumb_url = s3Service.uploadFile(
                createThumbKey(type.getLocation(), uuid, "300", file.getContentType())
                , resizedFile);

        // Middle 데이터 (Qna)
        resizedFile = resizeImage(file, 600); // 원하는 크기로 조정
        String middle_url = s3Service.uploadFile(
                createThumbKey(type.getLocation(), uuid, "600", file.getContentType())
                , resizedFile);
        log.debug("url : {}, thumb_url : {}, middle_url : {}", url, thumb_url, middle_url);

        // db
        Attach attach = Attach.builder()
                .url(url)
                .name(file.getOriginalFilename())
                .uuid(uuid)
                .contentType(file.getContentType())
                .type(type)
                .size(file.getSize())
                .build();
        attachRepository.save(attach);

        return attach;
    }

    public String getFileUrl(String uuid) {
        // db
        Attach attach = attachRepository.findByUuid(uuid)
                .orElseThrow(() -> new AttachException(_NOT_FOUND));
        // s3
        return s3Service.getFileUrl(getKeyfromAttach(attach));
    }

    public Attach deleteFile(AttachType type, String uuid) {
        // db
        Attach attach = attachRepository.findByUuid(uuid)
                .orElseThrow(() -> new AttachException(_NOT_FOUND));
        // s3
        s3Service.deleteFile(getKeyfromAttach(attach));
        // db
        attachRepository.delete(attach);
        return attach;
    }

    public byte[] download(AttachType type, String uuid) {
        // db
        Attach attach = attachRepository.findByUuid(uuid)
                .orElseThrow(() -> new AttachException(_NOT_FOUND));
        // s3
        return s3Service.downloadFile(getKeyfromAttach(attach));
    }

    public String getUrl(String uuid) {
        // db
        Attach attach = attachRepository.findByUuid(uuid)
                .orElseThrow(() -> new AttachException(_NOT_FOUND));
        return attach.getUrl();
    }

    private String getKeyfromAttach(Attach attach) {
        return createKey(attach.getType().getLocation(), attach.getUuid(), attach.getContentType());
    }

    private MultipartFile resizeImage(MultipartFile originalFile, int targetWidth) {
        try {
            // MultipartFile을 BufferedImage로 변환
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalFile.getBytes()));

            // 원본 이미지의 비율 계산
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int targetHeight = (int) ((double) targetWidth / originalWidth * originalHeight);

            // 이미지 리사이즈
            Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

            // 리사이즈된 이미지를 BufferedImage로 변환
            BufferedImage bufferedResizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

            // 원본 파일의 확장자 가져오기
            String originalFilename = originalFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);


            // BufferedImage를 ByteArrayOutputStream으로 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedResizedImage, extension, baos);
            byte[] resizedBytes = baos.toByteArray();

            // 리사이즈된 이미지를 MultipartFile로 변환
            return new MultipartFile() {
                @Override
                public String getName() {
                    return originalFile.getName();
                }

                @Override
                public String getOriginalFilename() {
                    return originalFile.getOriginalFilename();
                }

                @Override
                public String getContentType() {
                    return originalFile.getContentType();
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
        } catch (IOException e) {
            throw new RuntimeException("Failed to resize image", e);
        }
    }
}
