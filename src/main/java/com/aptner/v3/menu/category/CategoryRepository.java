package com.aptner.v3.menu.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByMenuIdAndName(Long menuId, String name);
}
