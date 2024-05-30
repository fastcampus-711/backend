package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        private Boolean visible;

        public CommonPost toEntity() {
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

            Class<? extends CommonPost> entityClass = getEntityClassType();

            return modelMapper.map(this, entityClass);
        }

        private Class<? extends CommonPost> getEntityClassType() {
            return Arrays.stream(CategoryCode.values())
                    .filter(s -> s.getDtoForRequest().equals(this.getClass()))
                    .findFirst()
                    .orElseThrow()
                    .getDomain();
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
        private ReactionType reactionType = ReactionType.DEFAULT;
        private long countOfComments;
        private String dtype;
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

        public CommonPostDto.Response applyReactionTypeToComments(Map<Long, ReactionType> mapCommentIdAndReactionType) {
            for (CommentDto.Response comment : comments) {
                if (mapCommentIdAndReactionType.containsKey(comment.getId()))
                    comment.setReactionType(mapCommentIdAndReactionType.get(comment.getId()));
                for (CommentDto.Response childComment : comment.getChildComments())
                    if (mapCommentIdAndReactionType.containsKey(childComment.getId()))
                        childComment.setReactionType(mapCommentIdAndReactionType.get(childComment.getId()));
            }

            return this;
        }
    }
}
