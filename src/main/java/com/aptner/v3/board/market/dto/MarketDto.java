package com.aptner.v3.board.market.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.MarketStatus;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class MarketDto extends CommonPostDto {
    private String type;
    private MarketStatus status;

    public static MarketDto of(BoardGroup boardGroup, MemberDto memberDto, MarketDto.MarketRequest request) {

        return MarketDto.builder()
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

    public Market toEntity(Member member, Category category) {
        return Market.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.isVisible(),
                type,
                status
        );
    }

    @Override
    public MarketResponse toResponse() {

        MarketDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return MarketDto.MarketResponse.builder()
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
    @NoArgsConstructor
    public static class MarketRequest extends CommonPostDto.CommonPostRequest {
        private String type;
        private String status;

        public static MarketDto.MarketRequest of(Long id, Long categoryId) {
            return MarketDto.MarketRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        @Override
        public MarketDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return MarketDto.of(
                    boardGroup,
                    user.toDto(),
                    (MarketRequest) request
            );
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class MarketResponse extends CommonPostDto.CommonPostResponse {
        private String type;
        private String status;

    }
}
