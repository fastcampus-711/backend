package com.aptner.v3.global.util;

import com.aptner.v3.attach.AttachType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public final class MultipartUtil {
    private static final String BASE_DIR = AttachType.FILE.name();

    /**
     * 로컬에서의 사용자 홈 디렉토리 경로를 반환합니다.
     */
    public static String getLocalHomeDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * 새로운 파일 고유 ID를 생성합니다.
     * @return 36자리의 UUID
     */
    public static String createFileId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Multipart 의 ContentType 값에서 / 이후 확장자만 잘라냅니다.
     * @param contentType ex) image/png
     * @return ex) png
     */
    public static String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return null;
    }

    /**
     * 파일명에서 확장자만 잘라냅니다.
     * @param filename 파일명
     */
    public static String getFormatFromName(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * 파일의 전체 경로를 생성합니다.
     * @param contentType 파일명
     */
    public static String createBasicKey(String contentType) {
        return String.format("%s/%s.%s", BASE_DIR, createFileId(), getFormat(contentType));
    }

    /**
     * 파일의 전체 경로를 생성합니다.
     * @param location 위치
     * @param contentType 확장자명
     */
    public static String createKey(String location, String uuid, String contentType) {
        return String.format("%s/%s.%s", location, uuid, getFormat(contentType));
    }

    /**
     * 검증합니다.
     * @param type 유형
     * @param multipartFile 파일
     */
    public static boolean isFileValid(AttachType type, MultipartFile multipartFile) {
        // 파일 크기 검증
        if (multipartFile.getSize() > type.getMaxSize()) {
            return false;
        }

        // 파일 확장자 검증
        String extension = getFormat(multipartFile.getContentType());
        return type.getAllowedExtension().contains(extension);
    }

    /**
     * 이미지 리사이즈를 위해, File형 변환
     * @param location 파일 위치
     * @param data 파일
     */
    public static File toFile(String location, byte[] data) {

        File myFile = new File(location);
        try {
            // Write the data to a local file.
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not founded. can't trans to byte[] to file");
        } catch (IOException e) {
            System.out.println("trouble with byte[] to File");
        }
        return myFile;
    }

}