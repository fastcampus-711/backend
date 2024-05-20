package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.service.CountOfReactionAndCommentApplyService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService extends CountOfReactionAndCommentApplyService<Comment> {
    private final CommentRepository commentRepository;
    private final CommonPostRepository<CommonPost> commonPostRepository;

    public CommentService(CommentRepository commentRepository, CommonPostRepository<CommonPost> commonPostRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.commonPostRepository = commonPostRepository;
    }

    public CommentDto.Response addComment(long postId, Long commentId, CommentDto.Request requestDto) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        Comment comment;
        if (commentId == null) {
            comment = Comment.of(commonPost, requestDto);
        } else {
            Comment parentComment = commentRepository.findById(commentId)
                    .orElseThrow(InvalidTableIdException::new);

            comment = Comment.of(parentComment, requestDto);
        }
        comment = commentRepository.save(comment);

        commonPost.updateCountOfComments(countComments(commonPost.getComments()));

        return comment.toResponseDto();
    }

    public CommentDto.Response updateComment(long postId, long commentId, CommentDto.Request requestDto) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(InvalidTableIdException::new)
                .updateByRequestDto(requestDto);

        commentRepository.save(comment);

        commonPost.updateCountOfComments(countComments(commonPost.getComments()));

        return comment.toResponseDto();
    }

    public long deleteComment(long postId, long commentId) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        commentRepository.deleteById(commentId);

        commonPost.updateCountOfComments(countComments(commonPost.getComments()));

        return commentId;
    }
}
