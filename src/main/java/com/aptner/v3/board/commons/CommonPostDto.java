package com.aptner.v3.board.commons;

import com.aptner.v3.comment.CommentDto;
import com.aptner.v3.reaction.domain.ReactionColumns;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CommonPostDto {
    @Getter
    @ToString
    public static class Request {
        @NotBlank
        private long categoryId;
        @NotBlank
        private String title;
        @NotBlank
        private String content;

        private boolean visible;

        public CommonPost toEntity() {
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

            return modelMapper.map(this, CommonPost.class);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private long id;
        private long userId;
        private long postUserId;
        private long categoryId;
        private String title;
        private String content;
        private boolean visible;
        private int hits;
        private ReactionColumns reactionColumns;
        private long countOfComments;
        private List<CommentDto.Response> comments;

        public <E extends CommonPost> Response(E entity) {
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

            modelMapper.map(entity, this);
        }

        public CommonPostDto.Response blindPostAlgorithm() {
            if (!visible && MemberUtil.getMemberId() != userId) {
                this.title = "비밀 게시글입니다.";
                this.content = "비밀 게시글입니다.";
                this.reactionColumns.blindColumns();
                this.comments = new ArrayList<>();
            }
            blindCommentAlgorithm(comments);

            return this;
        }

        private void blindCommentAlgorithm(List<CommentDto.Response> comments) {
            if (comments == null)
                return;

            for (int i = 0; i < comments.size(); i++) {
                CommentDto.Response comment = comments.get(i);
                if (comment.isAdmin()) {
                    comments.add(0, comments.remove(i));
                }

                if (!comment.isVisible() &&
                        MemberUtil.getMemberId() != userId &&
                        MemberUtil.getMemberId() != postUserId) {
                    comment.setContent("비밀 댓글입니다.");
                    comment.getReactionColumns().blindColumns();
                }
                blindCommentAlgorithm(comment.getChildComments());
            }
        }
    }
}
