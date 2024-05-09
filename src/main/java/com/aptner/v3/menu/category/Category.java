package com.aptner.v3.menu.category;

import com.aptner.v3.menu.menu.Menu;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Table(name = "Categories", indexes = {
        @Index(columnList = "name"),
})
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @ManyToOne(optional = false)
    @JsonBackReference
    private Menu menu;
}
