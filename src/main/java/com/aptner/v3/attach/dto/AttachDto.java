package com.aptner.v3.attach.dto;

import com.aptner.v3.attach.Attach;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


public class AttachDto {

    @Getter
    public static class AttachRequest {
        private String uuid;
    }

    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AttachResponse {
        private Long id;
        private String name;
        private String uuid;
        private String url;
        private String contentType;
        private Long size;
        private CommonPost post;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String createdBy;
        private String modifiedBy;

    }

    public static AttachResponse from(Attach attach) {
        return new AttachResponse(
                attach.getId(),
                attach.getName(),
                attach.getUuid(),
                attach.getUrl(),
                attach.getContentType(),
                attach.getSize(),
                attach.getPost(),
                attach.getCreatedAt(),
                attach.getModifiedAt(),
                attach.getCreatedBy(),
                attach.getModifiedBy()
        );
    }

}
