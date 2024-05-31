package com.aptner.v3.board.complain.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ComplainDto extends CommonPostDto {

    public static ComplainDto of(BoardGroup boardGroup, MemberDto memberDto, ComplainDto.ComplainRequest request) {

        return ComplainDto.builder()
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

    public Complain toEntity(Member member, Category category) {
        return Complain.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.isVisible()
        );
    }

    public ComplainDto.ComplainResponse toResponse() {

        ComplainDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return ComplainDto.ComplainResponse.builder()
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

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @AllArgsConstructor
    public static class ComplainRequest extends CommonPostDto.CommonPostRequest {
        public static ComplainDto.ComplainRequest of(Long id, Long categoryId) {
            return ComplainDto.ComplainRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        @Override
        public ComplainDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return ComplainDto.of(
                    boardGroup,
                    user.toDto(),
                    (ComplainDto.ComplainRequest) request
            );
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class ComplainResponse extends CommonPostDto.CommonPostResponse {

    }
}
