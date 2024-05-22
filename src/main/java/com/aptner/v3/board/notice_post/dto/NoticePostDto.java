package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NoticePostDto {

    @Getter
    public static class Request extends CommonPostDto.Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime postAt;

        public NoticePost toEntity() {
            return new NoticePost(getTitle(), getContent(), postAt);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {
        private LocalDateTime postAt;

        public <E extends CommonPost> Response(E entity) {
            super(entity);
        }
    }
}
