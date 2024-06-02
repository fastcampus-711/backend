package com.aptner.v3.board.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.commonPost.id = :postId")
    Page<Comment> findAllByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.commonPost.id = :postId ORDER BY c.isAdminComment DESC, c.createdAt DESC")
    Page<Comment> findAllByPostIdSorted(@Param("postId") Long postId, Pageable pageable);
}
