package com.aptner.v3.board.free_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
                .blindAt(request.getBlindAt())
                .blindBy(request.getBlindBy())
                .build();
    }

    public FreePost toEntity(Member member, Category category) {
        return FreePost.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.isVisible(),
                blindAt,
                blindBy
        );
    }

    public FreePostResponse toResponse(FreePostDto dto) {

        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return FreePostResponse.builder()
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
                .blindBy(dto.getBlindBy())
                .blindAt(dto.getBlindAt())
                .build();
    }

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
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
