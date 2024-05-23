package com.aptner.v3.board.commons;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.comment.CommentDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.reaction.domain.Reactions;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.aptner.v3.CommunityApplication.modelMapper;

@AllArgsConstructor
@NoArgsConstructor
public class CommonPostDto {
    Long id;
    Long boardGroupId;
    Long categoryId;
    Member member;
    String title;
    String content;
    List<String> imageUrls;
    boolean visible;

    public static CommonPostDto of(Member member, String title, String content, List<String> imageUrls, boolean visible) {
        return new CommonPostDto(null, null, null, member, title, content, imageUrls, visible);
    }

    public CommonPost toEntity() {
        return CommonPost.of(
            this.boardGroupId,
            this.categoryId,
            this.member,
            this.title,
            this.content,
            this.imageUrls,
            this.visible
        );
    }

    @Getter
    @ToString
    @SuperBuilder
    public static class CommonRequest {
        /* 제목 */
        @NotBlank
        String title;
        /* 내용 */
        @NotBlank
        String content;
        /* 이미지 저장 */
        List<String> imageUrls;
        /* 노출 여부 */
        final boolean visible = true;
        /* 작성자 */
        Long userId;

        public CommonPostDto toDto(Member member) { /* 여기에 principal */
            return CommonPostDto.of(
                    member,
                    title,
                    content,
                    imageUrls,
                    visible
            );
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CommonResponse {
        private long id;
        private long categoryId;
        private String title;
        private String content;
        private List<String> imageUrls;
        private long userId;
        private long postUserId;
        private boolean visible;
        private int hits;
        private Reactions reactions;
        private long countOfComments;
        private List<CommentDto.CommentResponse> comments;

        public <E extends CommonPost> CommonResponse(E entity) {
            modelMapper().map(entity, this);
        }

        public CommonResponse blindPostAlgorithm() {
            if (!visible && MemberUtil.getMemberId() != userId) {
                String secretPhaseOfPost = "비밀 게시글입니다.";
                this.title = secretPhaseOfPost;
                this.content = secretPhaseOfPost;
                this.reactions.blindColumns();
                this.comments = new ArrayList<>();
            }
            blindCommentAlgorithm(comments);

            return this;
        }

        private void blindCommentAlgorithm(List<CommentDto.CommentResponse> comments) {
            if (comments == null)
                return;

            for (int i = 0; i < comments.size(); i++) {
                CommentDto.CommentResponse comment = comments.get(i);
                if (comment.isAdmin()) {
                    comments.add(0, comments.remove(i));
                }

                if (!comment.isVisible() &&
                        MemberUtil.getMemberId() != userId &&
                        MemberUtil.getMemberId() != postUserId) {
                    String secretPhaseOfComment = "비밀 댓글입니다.";
                    comment.setContent(secretPhaseOfComment);
                    comment.getReactions().blindColumns();
                }
                blindCommentAlgorithm(comment.getChildComments());
            }
        }
    }
}