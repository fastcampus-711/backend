package com.aptner.v3.board.comment.repository;

import com.aptner.v3.board.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.commonPost.id = :postId")
    List<Comment> findAllByPostId(Long postId);
}
