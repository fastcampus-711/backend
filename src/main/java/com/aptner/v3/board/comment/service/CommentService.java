package com.aptner.v3.board.comment.service;

import com.aptner.v3.board.comment.repository.CommentRepository;
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

    public CommentDto.Response addComment(long postId, Long commentId, CommentDto.Request requestDto) {
        Comment comment;
        if (commentId == null) {
            CommonPost commonPost = commonPostRepository.findById(postId)
                    .orElseThrow(InvalidTableIdException::new);

            comment = Comment.of(commonPost, requestDto);
        } else {
            System.out.println("commentId : " + commentId);
            Comment parentComment = commentRepository.findById(commentId)
                    .orElseThrow(InvalidTableIdException::new);

            comment = Comment.of(parentComment, requestDto);
        }

        return commentRepository.save(comment)
                .toResponseDto();
    }

    public CommentDto.Response updateComment(long commentId, CommentDto.Request requestDto) {
        return commentRepository.findById(commentId)
                .orElseThrow(InvalidTableIdException::new)
                .update(requestDto)
                .toResponseDto();
    }

    public long deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
        return commentId;
    }

    public List<CommentDto.Response> getComment() {
        return commentRepository.findAll()
                .stream().map(Comment::toResponseDto)
                .toList();
    }
}
