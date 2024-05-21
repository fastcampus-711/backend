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

import java.util.ArrayList;
import java.util.List;

import static com.aptner.v3.global.error.ErrorCode._NOT_FOUND;
import static com.aptner.v3.global.util.MultipartUtil.createFileId;
import static com.aptner.v3.global.util.MultipartUtil.createKey;

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

        // s3
        String url = s3Service.uploadFile(key, file);

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
}
