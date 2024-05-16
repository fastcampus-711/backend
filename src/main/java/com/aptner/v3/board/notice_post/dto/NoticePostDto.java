package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class NoticePostDto {

    @Getter
    public static class Request extends CommonPostDto.Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime postAt;

        private List<String> imageUrls;

        public NoticePost toEntity() {
            return new NoticePost(getTitle(), getContent(), postAt);
        }

        public NoticePost toEntity(List<String> imageUrls) {
            return new NoticePost(getTitle(), getContent(), postAt, imageUrls);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {
        private LocalDateTime postAt;
        private List<String> imageUrls;

        public <E extends CommonPost> Response(E entity) {
            super(entity);
        }
    }
}
