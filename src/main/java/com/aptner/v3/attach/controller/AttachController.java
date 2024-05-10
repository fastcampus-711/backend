package com.aptner.v3.attach.controller;

import com.aptner.v3.attach.Attach;
import com.aptner.v3.attach.AttachType;
import com.aptner.v3.attach.dto.AttachDto;
import com.aptner.v3.attach.service.AttachService;
import com.aptner.v3.attach.service.ProfileService;
import com.aptner.v3.global.exception.AttachException;
import com.aptner.v3.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.aptner.v3.global.error.ErrorCode._EMPTY_FILE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "첨부 파일")
public class AttachController {

    private final AttachService attachService;
    private final ProfileService profileService;

    //    @preAuthorize("isAuthenticated()")
    @PostMapping(value = "/attach", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "첨부 파일 등록")
    public ResponseEntity<?> uploadAttach(@RequestPart(value = "files") List<MultipartFile> files
//            , Authentication authentication
    ) {
//        User user = (User) authentication.getPrincipal();

        // 다중 파일 처리 로직
        List<Attach> attaches = attachService.uploads(AttachType.FILE, files);
        return ResponseEntity.ok(attaches);
    }

    @GetMapping("attach/{uuid}")
    @Operation(summary = "첨부 파일 다운로드")
    public ResponseEntity<byte[]> download(@PathVariable("uuid") String uuid) {
        byte[] filebytes = attachService.download(AttachType.FILE, uuid);
        String url = attachService.getUrl(uuid);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + url.substring(url.lastIndexOf('/') + 1))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(filebytes.length)
                .body(filebytes);
    }

    @PostMapping(value = "/user/{id}/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "사용자 프로필 등록")
    public ResponseEntity<User> uploads(@PathVariable("id") Long userId
            , @RequestPart(value = "file") MultipartFile file
    ) {
        log.info("Received user ID: {}", userId);
        if (file.isEmpty()) {
            throw new AttachException(_EMPTY_FILE);
        }
        User updated = profileService.updateUserProfile(userId, file);
        return ResponseEntity.ok(updated);
    }

}
