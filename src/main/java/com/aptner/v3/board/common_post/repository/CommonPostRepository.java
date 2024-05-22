package com.aptner.v3.board.common_post.repository;

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
    List<T> findByDtype(String dtype);

    Page<T> findByTitleContaining(String keyword, Pageable pageable);

    Page<T> findByTitleContainingAndDtype(String keyword, String dtype, Pageable pageable);

    Optional<T> findByComments_CommonPostId(long postId);

    List<T> findByCategoryId(Long categoryId);

    //7일 이내의 조회수+공감수가 가장 높은 3개의 글을 조회
    /*@Query("SELECT p FROM CommonPost p WHERE p.createdAt >= CURRENT_DATE - 7 ORDER BY (p.hits + p.countReactionTypeGood) DESC")
    List<T> findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfter(Pageable pageable);*/

    //@Query("SELECT * FROM common_post WHERE created_at > NOW() - 7 ORDER BY hits DESC")
    //@Query("SELECT p FROM CommonPost p WHERE p.createdAt >= local datetime - 7 ORDER BY p.hits DESC")
    @Query("SELECT p FROM CommonPost p WHERE p.createdAt >= :sevenDayAgo ORDER BY p.hits DESC")
    List<T> findTop3ByOrderByHitsDescAndCreatedAtAfter(@Param("sevenDayAgo") LocalDateTime sevenDayAgo, PageRequest of);
}
