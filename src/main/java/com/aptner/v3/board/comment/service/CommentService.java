package com.aptner.v3.board.comment.service;

import com.aptner.v3.board.comment.CommentRepository;
import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.comment.dto.CommentDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getComments(long postId) {
        return commentRepository.findAllByPostId(postId);
    }


    public void addComment(long postId, CommentDto.AddRequest requestDto) {
        Comment comment = Comment.of(postId, requestDto);
    }
}
