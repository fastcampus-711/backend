package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class FreePostDto extends CommonPostDto {
    @Getter
    public static class Request extends CommonPostDto.Request {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime blindAt;
        private String blindBy;
        private List<String> imageUrls;
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
