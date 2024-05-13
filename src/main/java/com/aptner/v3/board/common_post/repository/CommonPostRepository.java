package com.aptner.v3.board.common_post.repository;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;


public interface CommonPostRepository<T extends CommonPost> extends JpaRepository<T, Long> {
    List<T> findByDtype(String dtype);

    Page<T> findByTitleContaining(String keyword, Pageable pageable);

    Page<T> findByTitleContainingAndDtype(String keyword, String dtype, Pageable pageable);
}
