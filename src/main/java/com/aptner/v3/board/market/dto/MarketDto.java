package com.aptner.v3.board.market.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
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
    private String type;  // @todo check type이 뭐징?
    private MarketStatus status;
    private Integer price;

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
                .status(request.getStatus())
                .price(request.getPrice())
                .boardGroup(boardGroup)
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .createdBy(null)
                .createdAt(null)
                .modifiedAt(null)
                .modifiedBy(null)
                .build();
    }

    public static MarketDto fromMarketEntity(Market entity) {

        return MarketDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(entity.isVisible())
                .boardGroup(BoardGroup.getByTable(entity.getDtype()))
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public Market toEntity(Member member, Category category) {
        return Market.of(
                member,
                category,
                this.getTitle(),
                this.getContent(),
                this.getImageUrls(),
                this.isVisible(),
                type,
                status,
                price
        );
    }

    @Override
    public MarketResponse toResponse() {

        MarketDto dto = this;
        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return MarketResponse.builder()
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
                // market
                .price(dto.getPrice())
                .status(dto.getStatus())
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
    public static class MarketRequest extends CommonPostDto.CommonPostRequest {
        private String type;
        private MarketStatus status;
        private Integer price;

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
        private MarketStatus status;
        private Integer price;
    }
}
