package com.aptner.v3.board.market;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("MarketPost")
public class Market extends CommonPost {
    private String type;
    private String status;

    public Market() {}

    public Market(Member member, Category category, String title, String content, boolean visible, String type, String status) {
        super(member, category, title, content, visible);
        this.type = type;
        this.status = status;
    }

    public static Market of(Member member, Category category, String title, String content, boolean visible, String type, String status) {
        return new Market(member, category, title, content, visible, type, status);
    }
}
