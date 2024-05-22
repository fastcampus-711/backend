package com.aptner.v3.attach.dto;

import com.aptner.v3.attach.Attach;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;


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
    }

    public static AttachResponse from(Attach attach) {
        return new AttachResponse(
                attach.getId(),
                attach.getName(),
                attach.getUuid(),
                attach.getUrl(),
                attach.getContentType(),
                attach.getSize(),
                attach.getPost()
        );
    }

}
