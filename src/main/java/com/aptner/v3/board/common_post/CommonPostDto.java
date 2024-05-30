package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
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

import java.util.ArrayList;
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
    Long hits;
    ReactionColumnsDto reactionColumnsDto;
    Long countOfComments;
    boolean visible;
    BoardGroup boardGroup;
    CategoryDto categoryDto;

    public static CommonPostDto of(BoardGroup boardGroup, MemberDto memberDto, CommonPostRequest request) {

        return FreePostDto.builder()
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

    public static CommonPostDto from(CommonPost entity) {

        log.debug("dtype : {}", entity.getDtype());
        CommonPostDto build = CommonPostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(MemberUtil.getMemberId() != entity.getMember().getId())
                .boardGroup(BoardGroup.getByTable(entity.getDtype()))
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();

        return build;
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
        protected boolean visible;
        protected Long hits;
        protected ReactionColumnsDto reactionColumns;
        protected long countOfComments;
        protected BoardGroup boardGroup;
        protected List<CommentDto.Response> comments;

        public CommonPostResponse blindPostAlgorithm() {
            if (!visible && MemberUtil.getMemberId() != userId) {
                this.title = "비밀 게시글입니다.";
                this.content = "비밀 게시글입니다.";
//                this.reactionColumns.blindColumns();
                this.comments = new ArrayList<>();
            }
            blindCommentAlgorithm(comments);

            return this;
        }

        protected void blindCommentAlgorithm(List<CommentDto.Response> comments) {
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
//                    comment.getReactionColumns().blindColumns();
                }
                blindCommentAlgorithm(comment.getChildComments());
            }
        }

        public static CommonPostResponse from(CommonPostDto dto) {

            String blindTitle = "비밀 게시글입니다.";
            String blindContent = "비밀 게시글입니다.";
            boolean isSecret = hasSecret(dto);

            return CommonPostResponse.builder()
                    .id(dto.getId())
                    .userId(dto.getMemberDto().getId())
                    .userNickname(dto.getMemberDto().getNickname())
                    .userImage(dto.getMemberDto().getImage())
                    .visible(dto.isVisible())
                    .title(isSecret ? blindTitle : dto.getTitle())
                    .content(isSecret ? blindContent : dto.getContent())
                    .hits(dto.getHits())
                    .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                    .countOfComments(dto.getCountOfComments())
                    .boardGroup(dto.getBoardGroup())
                    .categoryName(dto.getCategoryDto().getName())
                    .createdAt(dto.getCreatedAt())
                    .createdBy(dto.getCreatedBy())
                    .modifiedAt(dto.getModifiedAt())
                    .modifiedBy(dto.getModifiedBy())
                    .build();

        }

        public static boolean hasSecret(CommonPostDto dto) {
            return (!dto.isVisible()
                    && MemberUtil.getMemberId() != dto.getMemberDto().getId());
        }
    }
}
