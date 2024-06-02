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

import static com.aptner.v3.board.comment.CommentDto.CommentResponse.hasAdminRole;
import static com.aptner.v3.board.comment.CommentDto.CommentResponse.isOwner;

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
    // comment info
    ReactionColumnsDto reactionColumnsDto;
    boolean visible;
    boolean isAdminComment;

    public static CommentDto of(Long postId, MemberDto memberdto, Long commentId) {
        return CommentDto.of(postId, memberdto, commentId, null, null, true, false);
    }
    public static CommentDto of(Long postId, MemberDto memberdto, Long commentId, Long parentCommentId, String content, boolean visible, boolean isAdminComment) {
        return CommentDto.builder()
                .postId(postId)
                .memberDto(memberdto)
                .commentId(commentId)
                .parentCommentId(parentCommentId)
                .content(content)
                .visible(visible)
                .isAdminComment(isAdminComment)
                .build();
    }

    public Comment toEntity(CommonPost commonPost, Member member) {
        return Comment.of(
                commonPost,
                member,
                content,
                visible,
                CommentResponse.hasAdminRole()
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
                .visible(dto.isVisible())
                .isAdminComment(dto.isAdminComment())
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
        private ReactionType reactionType = ReactionType.DEFAULT;
        private boolean visible;
        // icon
        private boolean isAdminComment;
        private boolean isOwner;

        public boolean hasParentComment() {
            return parentCommentId != null;
        }

        public static boolean hasSecret(CommentDto dto) {
            // isVisible: false && (user != writer)
            return (!dto.isVisible()
                    && !MemberUtil.getMember().getId().equals(dto.getMemberDto().getId()));
        }

        public static boolean isOwner(CommentDto dto) {
            return MemberUtil.getMember().getId().equals(dto.getMemberDto().getId());
        }

        public static boolean hasAdminRole() {
            return MemberUtil.getMember().getRoles().contains(MemberRole.ROLE_ADMIN);
        }
    }
}
