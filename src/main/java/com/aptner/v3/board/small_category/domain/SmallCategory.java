package com.aptner.v3.board.small_category.domain;

import com.aptner.v3.board.SmallCategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.big_category.domain.BigCategory;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class SmallCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private SmallCategoryName smallCategoryName;
    @ManyToOne
    @JoinColumn(name = "big_category_id")
    private BigCategory bigCategory;

    @Transient
    private List<CommonPost> commonPost;

    public SmallCategory() {
    }

    private SmallCategory(SmallCategoryName smallCategoryName, BigCategory bigCategory) {
        this.smallCategoryName = smallCategoryName;
        this.bigCategory = bigCategory;
    }

    public static SmallCategory of(SmallCategoryName smallCategoryName, BigCategory bigCategory) {
        return new SmallCategory(smallCategoryName, bigCategory);
    }
}
