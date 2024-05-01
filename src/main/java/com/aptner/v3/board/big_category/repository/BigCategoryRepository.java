package com.aptner.v3.board.big_category.repository;

import com.aptner.v3.board.big_category.domain.BigCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BigCategoryRepository extends JpaRepository<BigCategory, Long> {
    @Modifying
    @Query("UPDATE BigCategory b SET b.name = ?2 WHERE b.id = ?1")
    void updateName(long target, String to);

}
