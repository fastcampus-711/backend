package com.aptner.v3.board.small_category.repository;

import com.aptner.v3.board.small_category.domain.SmallCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Long> {
    @Modifying
    @Query("UPDATE SmallCategory s SET s.smallCategoryName = ?2 WHERE s.id = ?1")
    void updateName(long id, String smallCategoryName);
}
