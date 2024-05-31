package com.aptner.v3.board.common_post;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
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

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPostDto extends BaseTimeDto {

    Long id;
    MemberDto memberDto;
    String title;
    String content;
    List<String> imageUrls;
    boolean visible;

    Long hits;
    @Setter
    private boolean isHot;
    ReactionColumnsDto reactionColumnsDto;
    Long countOfComments;
    protected List<Comment> comments;

    BoardGroup boardGroup;
    CategoryDto categoryDto;

    public static CommonPostDto of(BoardGroup boardGroup, MemberDto memberDto, CommonPostRequest request) {

        return CommonPostDto.builder()
                .id(request.getId())
                .memberDto(memberDto)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .hits(null)
                .reactionColumnsDto(null)
                .countOfComments(null)
                .visible(request.isVisible())
                .boardGroup(boardGroup)
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .createdBy(null)
                .createdAt(null)
                .modifiedAt(null)
                .modifiedBy(null)
                .build();
    }

    public CommonPost toEntity(Member member, Category category) {
        return CommonPost.of(
                member,
                category,
                title,
                content,
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
                // post
                .boardGroup(dto.getBoardGroup())
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
    @JsonPropertyOrder({"id", "user_id", "userNickname", "userImage", "categoryName", "title", "content", "visible", "reactionColumns", "countOfComments", "hits", "comments"})
    public static class CommonPostResponse extends BaseTimeDto.BaseResponse {
        protected long id;
        protected long userId;
        protected String userNickname;
        protected String userImage;
        protected long postUserId;
        protected String categoryName;
        protected String title;
        protected String content;
        protected List<String> imageUrls;
        protected boolean visible;

        protected Long hits;
        protected ReactionColumnsDto reactionColumns;
        protected long countOfComments;
        protected BoardGroup boardGroup;
        protected List<CommentDto.Response> comments;

        protected boolean isOwner;
        protected boolean isNew;
        protected boolean isHot;

        public static boolean hasSecret(CommonPostDto dto) {
            return (!dto.isVisible()
                    && MemberUtil.getMemberId() != dto.getMemberDto().getId());
        }

        public static boolean isOwner(CommonPostDto dto) {
            return (MemberUtil.getMemberId() == dto.getMemberDto().getId());
        }

        public static boolean isNew(CommonPostDto dto) {
            LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);
            return dto.getCreatedAt().isAfter(fourteenDaysAgo);
        }
    }
}
