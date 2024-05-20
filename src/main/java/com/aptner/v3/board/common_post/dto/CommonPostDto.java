package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
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
        private long countReactionTypeGood;
        private long countReactionTypeBad;
        private long countOfComments;
        private List<CommentDto.Response> comments;

        public <E extends CommonPost> Response(E entity) {
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

            modelMapper.map(entity, this);
        }
    }
}
