package com.aptner.v3.attach.service;

import com.aptner.v3.attach.AttachType;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.UserException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    private final S3Service s3Service;

    @Transactional
    public Member updateUserProfile(Long userId, MultipartFile file) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode._NOT_FOUND));

        String uuid = createFileId();
        String key = createKey(AttachType.PROFILE.getLocation(), uuid, file.getContentType());

        String url = s3Service.uploadFile(key, file);
        log.info(url);
        member.setImage(url);
        return memberRepository.save(member);
    }
}
