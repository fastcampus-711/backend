package com.aptner.v3.board.complain.dto;

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
public class ComplainDto extends CommonPostDto {
    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class ComplainRequest extends CommonPostDto.CommonPostRequest {

        public ComplainRequest(Long id, Long categoryId, String title, String content, Boolean visible, List<String> imageUrls) {
            super(id, categoryId, title, content, visible, imageUrls);
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    public static class ComplainResponse extends CommonPostDto.CommonPostResponse {

    }
}
