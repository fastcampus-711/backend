package com.aptner.v3.menu;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
@Entity
@Table(name = "MenuItems")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = true, name = "parent_id")
    @JsonBackReference  // 부모에 대한 순환 참조를 방지
    private MenuItem parent;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @ColumnDefault("1")
    // admin 1, front 2
    private int pageRole;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference  // 자식에 대한 순환 참조를 관리
    private List<MenuItem> items;

    public MenuItem(String name, MenuItem parent) {
        this.name = name;
        this.parent = parent;
    }

    public MenuItem(MenuItem parent, String code, String name) {
        this.code = code;
        this.name = name;
        this.parent = parent;
        this.pageRole = 1;
    }

    public MenuItem(MenuItem parent, String code, String name, int isAdmin) {
        this.code = code;
        this.name = name;
        this.parent = parent;
        this.pageRole = isAdmin;
    }
}