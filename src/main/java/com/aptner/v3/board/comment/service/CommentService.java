package com.aptner.v3.board.comment.service;

import com.aptner.v3.board.comment.CommentRepository;
import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.comment.dto.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommonPostRepository<CommonPost> commonPostRepository;

    public List<Comment> getComments(long postId) {
        return commentRepository.findAllByPostId(postId);
    }


    public void addComment(long postId, CommentDto.Request requestDto) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        Comment comment = Comment.of(commonPost, requestDto.getContent());

        commentRepository.save(comment);
    }

    public void updateComment(long commentId, CommentDto.Request requestDto) {
        commentRepository.findById(commentId)
                .orElseThrow(InvalidTableIdException::new)
                .update(requestDto);
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }
}
