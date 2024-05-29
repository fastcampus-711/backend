package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
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
    public static class FreePostRequest extends CommonPostDto.CommonPostRequest {
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime blindAt;
        private String blindBy;

        public FreePostRequest(Long id, Long categoryId, String title, String content, boolean visible, List<String> imageUrls, LocalDateTime blindAt, String blindBy) {
            super(id, categoryId, title, content, visible, imageUrls);
            this.blindAt = blindAt;
            this.blindBy = blindBy;
        }
    }

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class FreePostResponse extends CommonPostDto.CommonPostResponse {
        private LocalDateTime blindAt;
        private String blindBy;

    }
}
