package com.aptner.v3.board.menu.domain;

import com.aptner.v3.board.category.domain.Category;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "category_id")
    List<Category> categories;

    public Menu() {}

    public Menu(String name) {
        this.name = name;
    }
}
