package com.aptner.v3.board.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ListIndexBase;
import org.hibernate.annotations.SQLDelete;

import java.util.Objects;

@Getter
@Table(name = "categories", indexes = {
        @Index(columnList = "name"),
})
@Entity
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id = ?")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ListIndexBase(1)
    private long id;

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(updatable = false)
    private Long menuId;  // 단방형 연결을 위해 객체가 아닌 Long 타입 선언

    private Boolean deleted = Boolean.FALSE;

    protected Category() {}
    public Category(String name, String code, Long menuId) {
        this.code = code;
        this.name = name;
        this.menuId = menuId;
    }

    public static Category of(String code, String name, Long menuId) {
        return new Category(code, name, menuId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
