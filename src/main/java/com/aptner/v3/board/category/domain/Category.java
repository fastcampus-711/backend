package com.aptner.v3.board.category.domain;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.menu.domain.Menu;
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
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Transient
    private List<CommonPost> commonPost;

    public Category() {
    }

    private Category(CategoryName categoryName, Menu menu) {
        this.categoryName = categoryName;
        this.menu = menu;
    }

    public static Category of(CategoryName categoryName, Menu menu) {
        return new Category(categoryName, menu);
    }
}
