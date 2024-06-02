package com.aptner.v3.board.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query("SELECT c FROM Comment c WHERE c.commonPost.id = :postId")
//    Set<Comment> findAllByPostId(Long postId);
}
