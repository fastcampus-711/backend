package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long> {
    Page<T> findByDtype(String dtype, Pageable pageable);

    Page<T> findByTitleContainingIgnoreCaseAndVisible(String keyword, Pageable pageable, boolean visible);

    Page<T> findByTitleContainingIgnoreCaseAndDtypeAndVisible(String keyword, String dtype, Pageable pageable, boolean visible);

    Optional<T> findByComments_CommonPostId(long postId);

    List<T> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM CommonPost p " +
            "WHERE p.dtype = 'FreePost' " +
            "AND p.createdAt >= :sevenDaysAgo " +
            "AND p.visible = true " +
            "AND p.deleted = false " +
            "ORDER BY (p.hits + p.reactionColumns.countReactionTypeGood) DESC")
    List<T> findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfterAndDtype(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, PageRequest of);
}
