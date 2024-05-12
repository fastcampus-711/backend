package com.aptner.v3.menu;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ListIndexBase;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "menu", indexes = {
        @Index(columnList = "code"),
})
@Entity
@SQLDelete(sql = "UPDATE menu SET deleted = true WHERE id = ?")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ListIndexBase(1)
    private long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String code;

    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(updatable = false)
    private Long parentId; // 단방형 연결을 위해 객체가 아닌 Long 타입 선언

    @ToString.Exclude
//    @OrderBy("createdAt ASC ")
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.MERGE)
    private List<Menu> sub = new ArrayList<>();

    private Boolean deleted = Boolean.FALSE;

    protected Menu() {
    }

    protected Menu(String code, String name, Long parentId) {
        this.code = code;
        this.name = name;
        this.parentId = parentId;
    }

    public static Menu of(String code, String name, Long parentId) {
        return new Menu(code, name, parentId);
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
