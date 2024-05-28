package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class FreePostDto extends CommonPostDto {
    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class CommonPostRequest extends CommonPostDto.CommonPostRequest {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime blindAt;
        private String blindBy;

        public CommonPostRequest(Long id, Long categoryId, String title, String content, Boolean visible, List<String> imageUrls, LocalDateTime blindAt, String blindBy) {
            super(id, categoryId, title, content, visible, imageUrls);
            this.blindAt = blindAt;
            this.blindBy = blindBy;
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    public static class CommonPostResponse extends CommonPostDto.CommonPostResponse {
        private LocalDateTime blindAt;
        private String blindBy;
        private List<String> imageUrls;

        public <E extends CommonPost> CommonPostResponse(E entity) {
            super(entity);
        }
    }
}
