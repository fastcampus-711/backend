package com.aptner.v3.board.market.dto;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.global.util.MemberUtil;
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
    private String status;

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

    public static MarketDto from(Market entity) {

        return MarketDto.builder()
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
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class MarketResponse extends CommonPostDto.CommonPostResponse {
        private String type;
        private String status;

        public static MarketDto.MarketResponse from(MarketDto dto) {

            String blindTitle = "비밀 게시글입니다.";
            String blindContent = "비밀 게시글입니다.";
            boolean isSecret = hasSecret(dto);

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
    }
}
