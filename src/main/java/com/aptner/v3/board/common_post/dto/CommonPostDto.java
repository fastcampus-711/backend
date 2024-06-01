package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.global.domain.BaseTimeDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPostDto extends BaseTimeDto {

    Long id;
    // user
    MemberDto memberDto;
    // post
    String title;
    String content;
    List<String> imageUrls;
    boolean visible;
    Set<Comment> comments;
    // post info
    Long hits;
    ReactionColumnsDto reactionColumnsDto;
    Long countOfComments;
    // category
    String boardGroup;
    CategoryDto categoryDto;
    // icon
    @Setter
    private boolean isHot;

    public static CommonPostDto of(BoardGroup boardGroup, MemberDto memberDto, CommonPostRequest request) {

        return FreePostDto.builder()
                .id(request.getId())
                .memberDto(memberDto)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .visible(request.isVisible())
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .build();
    }

    public CommonPost toEntity(Member member, Category category) {
        return CommonPost.of(
                member,
                category,
                title,
                content,
                imageUrls,
                visible
        );
    }

    public CommonPostResponse toResponse() {
        CommonPostDto dto = this;

        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return CommonPostResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(isSecret ? blindTitle : dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                // post info
                .hits(dto.getHits())                                            // 조회수
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto()) // 공감
                .countOfComments(dto.getCountOfComments())                      // 댓글 수
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // base
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                // icon
                .isOwner(CommonPostResponse.isOwner(dto))
                .isNew(CommonPostResponse.isNew(dto))
                .isHot(dto.isHot())
                .build();
    }

    @Getter
    @Setter
    @ToString
    @SuperBuilder
    @NoArgsConstructor
    public static class CommonPostRequest {
        protected Long id;
        @NotBlank
        @Min(1L)
        protected Long categoryId;
        @NotBlank
        protected String title;
        @NotBlank
        protected String content;
        protected boolean visible;
        protected List<String> imageUrls;

        public static CommonPostDto.CommonPostRequest of(Long id, Long categoryId) {
            return CommonPostDto.CommonPostRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        public CommonPostDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return CommonPostDto.of(
                    boardGroup,
                    user.toDto(),
                    request
            );
        }

    }

    @Getter
    @Setter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonPropertyOrder({"id", "user_id", "userNickname", "userImage", "categoryName", "title", "content", "imageUrls", "visible", "reactionColumns", "countOfComments", "hits", "comments"})
    public static class CommonPostResponse extends BaseTimeDto.BaseResponse {
        protected long id;
        // user
        protected long userId;
        protected String userNickname;
        protected String userImage;
        // post
        protected String title;
        protected String content;
        protected List<String> imageUrls;
        protected boolean visible;
        // post info
        protected Long hits;
        protected ReactionColumnsDto reactionColumns;
        private ReactionType reactionType = ReactionType.DEFAULT;
        protected long countOfComments;
        // category
        protected String boardGroup;
        protected long categoryId;
        protected String categoryName;
        // icon
        protected boolean isOwner;
        protected boolean isNew;
        protected boolean isHot;

        public static boolean hasSecret(CommonPostDto dto) {
            // isVisible: false && (user != writer)
            return (!dto.isVisible()
                    && !MemberUtil.getMember().getId().equals(dto.getMemberDto().getId()));
        }

        public static boolean isOwner(CommonPostDto dto) {
            return MemberUtil.getMember().getId().equals(dto.getMemberDto().getId());
        }

        public static boolean isNew(CommonPostDto dto) {
            LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);
            return dto.getCreatedAt().isAfter(fourteenDaysAgo);
        }

    }

//    @Getter
//    @Setter
//    @ToString(callSuper = true)
//    @NoArgsConstructor
//    @SuperBuilder
//    public static class CommonPostWithCommentResponse extends CommonPostResponse {
//        protected Set<CommentDto.CommentResponse> comments;
//
//        public CommonPostDto.CommonPostResponse applyReactionTypeToComments(Map<Long, ReactionType> mapCommentIdAndReactionType) {
//            for (CommentDto.CommentResponse comment : comments) {
//                if (mapCommentIdAndReactionType.containsKey(comment.getId()))
//                    comment.setReactionType(mapCommentIdAndReactionType.get(comment.getId()));
//                for (CommentDto.CommentResponse childComment : comment.getChildComments())
//                    if (mapCommentIdAndReactionType.containsKey(childComment.getId()))
//                        childComment.setReactionType(mapCommentIdAndReactionType.get(childComment.getId()));
//            }
//
//            return this;
//        }
//    }
}
