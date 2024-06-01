package com.aptner.v3.board.market;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("MarketPost")
public class Market extends CommonPost {
    private String type;
    @Setter
    @Enumerated(EnumType.STRING)
    private MarketStatus status;
    @Setter
    private Integer price;

    public Market() {
    }

    public Market(Member member, Category category, String title, String content, boolean visible, List<String> imageUrls, String type, MarketStatus status, Integer price) {
        super(member, category, title, content, visible, imageUrls);
        this.type = type;
        this.status = status;
        this.price = price;
    }

    public static Market of(Member member, Category category, String title, String content, boolean visible, List<String> imageUrls, String type, MarketStatus status, Integer price) {
        return new Market(member, category, title, content, visible, imageUrls, type, status, price);
    }

    @Override
    public MarketDto toDto() {

        Market entity = this;
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
                .status(entity.getStatus())
                .price(entity.getPrice())
                .boardGroup(BoardGroup.getByTable(entity.getDtype()))
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

}
