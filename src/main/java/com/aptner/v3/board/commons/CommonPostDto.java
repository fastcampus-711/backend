package com.aptner.v3.board.commons;

import com.aptner.v3.comment.CommentDto;
import com.aptner.v3.reaction.domain.ReactionColumns;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CommonPostDto {
    @Getter
    @ToString
    @SuperBuilder
    public static class CommonRequest {
        /* 게시판 구분 ID */
        private long boardGroupId;
        /* 카테고리 ID */
        @NotBlank
        private long categoryId;
        /* 제목 */
        @NotBlank
        private String title;
        /* 내용 */
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
    public static class CommonResponse {
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

        public <E extends CommonPost> CommonResponse(E entity) {
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

            modelMapper.map(entity, this);
        }

        public CommonResponse blindPostAlgorithm() {
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
