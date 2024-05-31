package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Qualifier("commonPostRepository")
public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long> {

    // 게시판 별 조회
    Page<T> findByDtype(String dtype, Pageable pageable);

    // 게시판 별 + 분류 별 조회
    Page<T> findByDtypeAndCategoryId(String dtype, Long CategoryId, Pageable pageable);

    // 게시판 별 + 분류 별 + 검색어 조회
    Page<T> findByDtypeAndCategoryIdAndTitleContainingIgnoreCase(String dtype, Long CategoryId, String title, Pageable pageable);

    Optional<T> findByComments_CommonPostId(long postId);

    List<T> findByCategoryId(Long categoryId);

    //7일 이내의 조회수+공감수가 가장 높은 3개의 글을 조회
    /*@Query("SELECT p FROM CommonPost p WHERE p.createdAt >= CURRENT_DATE - 7 ORDER BY (p.hits + p.countReactionTypeGood) DESC")
    List<T> findTop3ByOrderByHitsAndReactionCountDescAndCreatedAtAfter(Pageable pageable);*/

    //@Query("SELECT * FROM common_post WHERE created_at > NOW() - 7 ORDER BY hits DESC")
    //@Query("SELECT p FROM CommonPost p WHERE p.createdAt >= local datetime - 7 ORDER BY p.hits DESC")
    @Query("SELECT p FROM CommonPost p WHERE p.createdAt >= :sevenDayAgo And p.dtype = :dtype ORDER BY p.hits DESC")
    List<T> findTop3ByOrderByHitsDescAndCreatedAtAfterAndDtype(@Param("sevenDayAgo") LocalDateTime sevenDayAgo, @Param("dtype") String dtype, PageRequest of);
}
