package com.aptner.v3.menu;

import com.aptner.v3.board.category.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

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

    private Menu(MenuName code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Menu of(MenuName code, String name) {
        return new Menu(code, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id == menu.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
