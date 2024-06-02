package com.aptner.v3.board.comment;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.domain.BaseTimeDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static com.aptner.v3.board.comment.CommentDto.CommentResponse.*;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto extends BaseTimeDto {
    MemberDto memberDto;
    Long postId;
    // comment
    Long commentId;
    String content;
    Long parentCommentId;
    Set<CommentDto> childComments;
    Set<Long> childCommentAuthorIds;
    // comment info
    ReactionColumnsDto reactionColumnsDto;
    boolean visible;
    // icon
    @Setter
    ReactionType reactionType;
    boolean isTop;

    public static CommentDto of(Long postId, MemberDto memberdto, Long commentId) {
        return CommentDto.of(postId, memberdto, commentId, 0L, null, true, false);
    }
    public static CommentDto of(Long postId, MemberDto memberdto, Long commentId, Long parentCommentId, String content, boolean visible, boolean isTop) {
        return CommentDto.builder()
                .postId(postId)
                .memberDto(memberdto)
                .commentId(commentId)
                .parentCommentId(parentCommentId)
                .content(content)
                .visible(visible)
                .isTop(isTop)
                .build();
    }

    public Comment toEntity(CommonPost commonPost, Member member) {
        return Comment.of(
                commonPost,
                member,
                content,
                visible,
                isTop
        );
    }

    public CommentDto.CommentResponse toResponseDto() {
        CommentDto dto = this;
        String blindTitle = "비밀 댓글입니다.";
        boolean isSecret = CommentResponse.hasSecret(dto);

        Comparator<CommentDto.CommentResponse> childCommentComparator = Comparator
                .comparing(CommentDto.CommentResponse::getCreatedAt)
                .thenComparingLong(CommentDto.CommentResponse::getCommentId);

        return CommentResponse.builder()
                .postId(dto.getPostId())
                .userId(dto.getMemberDto().getId())
                .userImage(dto.getMemberDto().getImage())
                .userNickname(dto.getMemberDto().getNickname())
                // comment
                .commentId(dto.getCommentId())
                .content(isSecret? blindTitle : dto.getContent())
                .childComments(new TreeSet<>(childCommentComparator))
                .parentCommentId(dto.getParentCommentId())
                // comment info
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .visible(dto.isVisible())
                .isTop(dto.isTop())
                .isAdminComment(isAdmin(dto.getMemberDto()))
                .isOwner(isOwner(dto))
                // base
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                .build();
    }

    @Getter
    @Setter
    @ToString
    @SuperBuilder
    @NoArgsConstructor
    public static class CommentRequest {
        private Long postId;
        private Long parentCommentId;
        @NotBlank
        private String content;
        private boolean visible;
        private boolean isTop;

        // 부모 댓글
        public static CommentRequest of(Long postId, String content, boolean visible) {
            return CommentRequest.of(postId, null, content, visible);
        }

        // 자식 댓글
        public static CommentRequest of(Long postId, Long parentCommentId, String content, boolean visible) {
            return CommentRequest.builder()
                    .postId(postId)
                    .parentCommentId(parentCommentId)
                    .content(content)
                    .visible(visible)
                    .build();
        }

        public CommentDto toDto(Long postId, Long commentId, CustomUserDetails user) {
            return CommentDto.of(
                    postId,
                    user.toDto(),
                    commentId,
                    parentCommentId,
                    content,
                    visible,
                    hasAdminRole()
            );
        }
    }

    @Getter
    @Setter
    @SuperBuilder
    public static class CommentResponse extends BaseTimeDto.BaseResponse {
        private long postId;
        // user
        private long userId;
        private String userImage;
        private String userNickname;
        // comment
        private Long commentId;
        private String content;
        private Set<CommentResponse> childComments;
        private Long parentCommentId;
        // comment info
        private ReactionColumnsDto reactionColumns;
        private ReactionType reactionType;
        private boolean visible;
        // icon
        private boolean isTop;
        private boolean isOwner;
        private boolean isAdminComment;

        public boolean hasParentComment() {
            return parentCommentId != null;
        }

        public static boolean hasSecret(CommentDto dto) {
            // isVisible: false && (user != writer)
            return (!dto.isVisible()
                    && !hasAuthToSeeComment(dto));
        }

        public static boolean hasAuthToSeeComment(CommentDto dto) {

            Long currentUser = MemberUtil.getMember().getId();
            return ( currentUser.equals(dto.getMemberDto().getId()))             // 작성자 동일
                    || dto.getChildCommentAuthorIds().contains(currentUser);     // 하위 댓글 작성자
        }

        public static boolean isOwner(CommentDto dto) {
            return MemberUtil.getMember().getId().equals(dto.getMemberDto().getId());
        }

        public static boolean hasAdminRole() {
            return MemberUtil.getMember().getRoles().contains(MemberRole.ROLE_ADMIN);
        }

        public static boolean isAdmin(MemberDto dto) {
            return dto.getRoles().contains(MemberRole.ROLE_ADMIN);
        }
    }
}
