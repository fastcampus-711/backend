package com.aptner.v3.board.qna.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class QnaDto extends CommonPostDto {

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class QnaRequest extends CommonPostDto.CommonPostRequest {
        private String type;
        private String status;

        public QnaRequest(Long id, Long categoryId, String title, String content, boolean visible, List<String> imageUrls, String type, String status) {
            super(id, categoryId, title, content, visible, imageUrls);
            this.type = type;
            this.status = status;
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    public static class QnaResponse extends CommonPostDto.CommonPostResponse {
        private long id;
        private String type;
        private String status;
    }
}
