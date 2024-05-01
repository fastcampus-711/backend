package com.aptner.v3.board.big_category.domain;

import com.aptner.v3.board.small_category.domain.SmallCategory;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class BigCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "small_category_id")
    List<SmallCategory> smallCategories;

    public BigCategory() {}

    public BigCategory(String name) {
        this.name = name;
    }
}
