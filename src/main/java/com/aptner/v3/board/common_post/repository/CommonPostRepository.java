package com.aptner.v3.board.common_post.repository;

import com.aptner.v3.board.common_post.domain.CommonPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long> {
    List<T> findByDtype(String dtype);

    Page<T> findByTitleContaining(String keyword, Pageable pageable);

    Page<T> findByTitleContainingAndDtype(String keyword, String dtype, Pageable pageable);

    Optional<T> findByComments_CommonPostId(long postId);
}
