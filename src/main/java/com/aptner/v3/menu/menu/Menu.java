package com.aptner.v3.menu.menu;

import com.aptner.v3.menu.category.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "Menus", indexes = {
        @Index(columnList = "name"),
})
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuName code;

    private String name;

    @OneToMany(mappedBy = "menu")
    @JsonManagedReference
    private List<Category> categories;

    protected Menu() {}

    public Menu(MenuName code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Menu of(MenuName code, String name) {
        return new Menu(code, name);
    }

}
