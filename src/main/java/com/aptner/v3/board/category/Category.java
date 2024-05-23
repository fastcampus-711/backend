package com.aptner.v3.board.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ListIndexBase;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Objects;

@Getter
@Table(name = "categories", indexes = {
        @Index(columnList = "code"),
})
@Entity
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id = ?")
@Where(clause = "deleted is false")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ListIndexBase(1)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(updatable = false)
    private Long boardGroup;

    private Boolean deleted = Boolean.FALSE;

    protected Category() {}
    public Category(String name, String code, Long boardGroup) {
        this.code = code;
        this.name = name;
        this.boardGroup = boardGroup;
    }

    public static Category of(String code, String name, Long boardGroup) {
        return new Category(code, name, boardGroup);
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