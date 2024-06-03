package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.converter.LocalDateTimeAttributeConverter;
import jakarta.persistence.Convert;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("commonPostRepository")
public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    // 게시판 별 조회
    Page<T> findByDtype(String dtype, Pageable pageable);

    Optional<T> findByDtypeAndId(String dtype, long postId);

    // 7일 이내의 조회수(hits) + 공감수(reactionColumns.countReactionTypeGood) 높은 목록
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Query("SELECT p FROM CommonPost p WHERE p.createdAt >= :date And p.dtype = :dtype And deleted = false ORDER BY (p.hits + p.reactionColumns.countReactionTypeGood) DESC")
    List<T> findTopPosts(@Param("date") LocalDateTime date, @Param("dtype") String dtype, Pageable pageable);

}
