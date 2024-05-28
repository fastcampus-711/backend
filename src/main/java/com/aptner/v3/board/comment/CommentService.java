package com.aptner.v3.board.comment;

import com.aptner.v3.board.common.reaction.service.CountCommentsAndReactionApplyService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.global.util.MemberUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommonPostRepository<CommonPost> commonPostRepository;
    private final CountCommentsAndReactionApplyService<Comment> countOfReactionAndCommentApplyService;
    public CommentService(CommentRepository commentRepository, CommonPostRepository<CommonPost> commonPostRepository) {
        this.commentRepository = commentRepository;
        this.commonPostRepository = commonPostRepository;
        this.countOfReactionAndCommentApplyService = new CountCommentsAndReactionApplyService<>(commentRepository);
    }

    public CommentDto.Response addComment(long postId, Long commentId, CommentDto.Request requestDto) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        requestDto.setUserId(MemberUtil.getMemberId());

        Comment comment;
        if (commentId == null) {
            comment = Comment.of(commonPost, requestDto);
        } else {
            Comment parentComment = commentRepository.findById(commentId)
                    .orElseThrow(InvalidTableIdException::new);
            requestDto.setPostUserId(commonPost.getMemberId());
            comment = Comment.of(parentComment, requestDto);
        }
        comment = commentRepository.save(comment);

        commonPost.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(commonPost.getComments())
        );

        return comment.toResponseDto();
    }

    public CommentDto.Response updateComment(long postId, long commentId, CommentDto.Request requestDto) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(InvalidTableIdException::new)
                .updateByRequestDto(requestDto);

        commentRepository.save(comment);

        commonPost.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(commonPost.getComments()))
        ;

        return comment.toResponseDto();
    }

    public long deleteComment(long postId, long commentId) {
        CommonPost commonPost = commonPostRepository.findById(postId)
                .orElseThrow(InvalidTableIdException::new);

        commentRepository.deleteById(commentId);

        commonPost.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(commonPost.getComments())
        );

        return commentId;
    }
}
