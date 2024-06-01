package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
@Qualifier("commonPostRepository")
public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long> {

    // 게시판 별 조회
    Page<T> findByDtype(String dtype, Pageable pageable);

    // 게시판 별 + 분류 별 조회
    Page<T> findByDtypeAndCategoryId(String dtype, Long CategoryId, Pageable pageable);

    // 게시판 별 + 분류 별 + 검색어 조회
    Page<T> findByDtypeAndCategoryIdAndTitleContainingIgnoreCase(String dtype, Long CategoryId, String title, Pageable pageable);

    Page<T> findByTitleContainingIgnoreCaseAndVisible(String keyword, Pageable pageable, boolean visible);

    Page<T> findByTitleContainingIgnoreCaseAndDtypeAndVisible(String keyword, String dtype, Pageable pageable, boolean visible);

    Optional<T> findByComments_CommonPostId(long postId);

    List<T> findByCategoryId(Long categoryId);

    // Fetch posts excluding the top ones by hits
    @Query("SELECT p FROM CommonPost p WHERE p.dtype = :dtype And p.id NOT IN :topPostIds")
    Page<T> findAllExcludingTopPostsAndDtype(@Param("topPostIds") List<Long> topPostIds, @Param("dtype") String dtype, Pageable pageable);

    // 7일 이내의 조회수 + 공감수 가장 높은 3개의 글을 조회
    @Query("SELECT p FROM CommonPost p WHERE p.createdAt >= :date And p.dtype = :dtype ORDER BY (p.hits + p.reactionColumns.countReactionTypeGood) DESC")
    List<T> findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfterAndDtype(@Param("date") LocalDateTime date, @Param("dtype") String dtype, Pageable pageable);
}
