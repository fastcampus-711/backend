package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class CommonPostDto {

    public static void test() {

    }

    @Getter
    @ToString
    public static class Request {
        @NotBlank
        private String title;
        @NotBlank
        private String content;

        public CommonPost toEntity() {
            return new CommonPost(title, content);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private long id;
        private String title;
        private String content;
        private int hits;
        private List<CommentDto.Response> comments = new ArrayList<>();

        public <E extends CommonPost> Response(E entity) {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                    .setFieldMatchingEnabled(true)
                    .setSkipNullEnabled(true);

            modelMapper.map(entity, this);
        }
    }
}
