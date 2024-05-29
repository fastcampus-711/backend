package com.aptner.v3.board.notice_post.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class NoticePostDto extends CommonPostDto {

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class NoticeRequest extends CommonPostDto.CommonPostRequest {

        private LocalDateTime postAt;

        public NoticeRequest(Long id, Long categoryId, String title, String content, boolean visible, List<String> imageUrls, LocalDateTime postAt) {
            super(id, categoryId, title, content, visible, imageUrls);
            this.postAt = postAt;
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    public static class NoticeResponse extends CommonPostDto.CommonPostResponse {
        private LocalDateTime postAt;
    }
}
