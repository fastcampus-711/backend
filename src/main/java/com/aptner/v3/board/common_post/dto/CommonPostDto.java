package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class CommonPostDto {

    @Getter
    public static class CreateRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String content;

         public CommonPost toEntity() {
             return new CommonPost(title, content);
         }
    }

    @Getter
    public static class UpdateRequest {
        @NotNull
        private long id;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }
}
