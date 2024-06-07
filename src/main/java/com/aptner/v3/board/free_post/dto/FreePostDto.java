package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.aptner.v3.board.common_post.dto.CommonPostCommentDto.organizeChildComments;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FreePostDto extends CommonPostDto {
    LocalDateTime blindAt;
    String blindBy;

    public static FreePostDto of(BoardGroup boardGroup, MemberDto memberDto, FreePostRequest request) {

        return FreePostDto.builder()
                .id(request.getId())
                // member
                .memberDto(memberDto)
                // post
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .visible(request.isVisible())
                // frees
                .blindAt(request.getBlindAt())
                .blindBy(request.getBlindBy())
                // category
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .build();
    }

    public FreePost toEntity(Member member, Category category) {
        return FreePost.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.getImageUrls(),
                this.isVisible(),
                blindAt,
                blindBy
        );
    }

    @Override
    public FreePostResponse toResponse() {

        FreePostDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return FreePostResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                // post info
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // frees
                .blindBy(dto.getBlindBy())
                .blindAt(dto.getBlindAt())
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

    @Override
    public FreePostResponse toResponseWithComment() {

        FreePostDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return FreePostResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : dto.getImageUrls())
                .visible(dto.isVisible())
                .comments(organizeChildComments(dto.getCommentDto()))
                // post info
                .hits(dto.getHits())
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                .reactionType(isSecret ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // frees
                .blindBy(dto.getBlindBy())
                .blindAt(dto.getBlindAt())
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
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class FreePostRequest extends CommonPostDto.CommonPostRequest {
        private LocalDateTime blindAt;
        private String blindBy;

        public static FreePostRequest of(Long id, Long categoryId) {
            return FreePostRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        @Override
        public FreePostDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return FreePostDto.of(
                    boardGroup,
                    user.toDto(),
                    (FreePostRequest) request
            );
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class FreePostResponse extends CommonPostDto.CommonPostResponse {
        private LocalDateTime blindAt;
        private String blindBy;
    }
}
