package com.aptner.v3.board.common_post.repository;

import com.aptner.v3.board.common_post.domain.CommonPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommonPostRepository extends JpaRepository<CommonPost, Long> {
    @Query(value = "SELECT * FROM ?1", nativeQuery = true)
    List<CommonPost> findBySmallCategoryName(String name);
}
