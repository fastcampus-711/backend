package com.aptner.v3.board.category.domain;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    @Transient
    private List<CommonPost> commonPost;

    public Category() {
    }

//    public static Category of(CategoryName categoryName) {
//        return new Category(categoryName);
//    }
}
