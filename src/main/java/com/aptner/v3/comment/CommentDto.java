package com.aptner.v3.comment;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.member.Member;
import com.aptner.v3.reaction.domain.Reactions;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    Long id;
    Long postId;
    Member member;
    Long parentCommentId;
    boolean visible;
    String content;

    private static CommentDto of(Long postId, Member member, Long parentCommentId, boolean visible, String content) {
        return new CommentDto(
                null,
                postId,
                member,
                parentCommentId,
                visible,
                content
        );
    }

    public Comment toEntity(CommonPost post, Member member) {
        return Comment.of(
                post,
                member,
                this.content,
                this.visible
        );
    }

    @Getter
    @Setter
    public static class CommentRequest {
        /* 게시글 ID */
        Long postId;
        /* 상위 댓글 ID */
        Long parentCommentId;
        /* 공개 여부 */
        boolean visible = true;
        /* 내용 */
        @NotBlank
        String content;

        public CommentDto toDto(Long postId, Member member) {
            return CommentDto.of(
                    postId,
                    member,
                    parentCommentId,
                    visible,
                    content
            );
        }
    }

    @Getter
    @Setter
    public static class CommentResponse {
        private long id;
        private String content;
        private Reactions reactions;
        private boolean visible;
        private boolean admin;
        private long commonPostId;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<CommentResponse> childComments;
    }
}
