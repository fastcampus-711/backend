package com.aptner.v3.board.common_post;

import com.aptner.v3.board.common_post.domain.CommonPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long> {
    Page<T> findByDtype(String dtype, Pageable pageable);

    Page<T> findByTitleContainingIgnoreCaseAndVisible(String keyword, Pageable pageable, boolean visible);

    Page<T> findByTitleContainingIgnoreCaseAndDtypeAndVisible(String keyword, String dtype, Pageable pageable, boolean visible);

    Optional<T> findByComments_CommonPostId(long postId);

    List<T> findByCategoryId(Long categoryId);
}
