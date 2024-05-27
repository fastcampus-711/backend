package com.aptner.v3.board.category.repository;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.repository.querydsl.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    List<Category> findByBoardGroup(Long boardGroupId);

    Optional<Category> findByCode(String code);
}
