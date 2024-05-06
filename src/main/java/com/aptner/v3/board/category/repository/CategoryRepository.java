package com.aptner.v3.board.category.repository;

import com.aptner.v3.board.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Modifying
    @Query("UPDATE Category s SET s.categoryName = ?2 WHERE s.id = ?1")
    void updateName(long id, String categoryName);
}
