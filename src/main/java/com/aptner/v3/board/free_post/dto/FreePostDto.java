package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class FreePostDto {
    @Getter
    public static class Request extends CommonPostDto.Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime blindAt;
        private String blindBy;

        private List<String> imageUrls;
        public FreePost toEntity() {
            return new FreePost(getTitle(), getContent(), blindAt, blindBy);
        }

        public FreePost toEntity(List<String> imageUrls) {
            return new FreePost(getTitle(), getContent(), blindAt, blindBy, imageUrls);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {
        private LocalDateTime blindAt;
        private String blindBy;
        private List<String> imageUrls;

        public <E extends CommonPost> Response(E entity) {
            super(entity);
        }
    }
}
