package com.aptner.v3.attach.service;

import com.aptner.v3.attach.AttachType;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.user.domain.UserEntity;
import com.aptner.v3.user.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.aptner.v3.global.util.MultipartUtil.createFileId;
import static com.aptner.v3.global.util.MultipartUtil.createKey;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserDetailsRepository userRepository;

    private final S3Service s3Service;

    @Transactional
    public UserEntity updateUserProfile(Long userId, MultipartFile file) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode._NOT_FOUND));

        String uuid = createFileId();
        String key = createKey(AttachType.PROFILE.getLocation(), uuid, file.getContentType());

        String url = s3Service.uploadFile(key, file);
        log.info(url);
        user.setImage(url);
        return userRepository.save(user);
    }
}
