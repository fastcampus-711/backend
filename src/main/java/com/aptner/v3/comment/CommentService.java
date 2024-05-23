package com.aptner.v3.comment;

import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import com.aptner.v3.reaction.service.CountOfReactionAndCommentApplyService;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.global.exception.custom.InvalidTableIdException;
import com.aptner.v3.global.util.MemberUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommonPostRepository<CommonPost> commonPostRepository;
    private final CountOfReactionAndCommentApplyService<Comment> countOfReactionAndCommentApplyService;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, CommonPostRepository<CommonPost> commonPostRepository,
                          MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.commonPostRepository = commonPostRepository;
        this.countOfReactionAndCommentApplyService = new CountOfReactionAndCommentApplyService<>(commentRepository);
        this.memberRepository = memberRepository;
    }

    public long deleteComment(long postId, long commentId) {
        // check
        CommonPost post = existsPostId(postId);

        // save
        commentRepository.deleteById(commentId);
        post.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(post.getComments())
        );

        return commentId;
    }

    public CommentDto.CommentResponse addComment(long postId, CommentDto.CommentRequest request) {
        // before
        Member member = memberRepository.getReferenceById(MemberUtil.getMemberId());
        CommentDto dto = request.toDto(postId, member);

        // check
        CommonPost post = existsPostId(postId);

        // new
        Comment newComment = dto.toEntity(post, member);
        if (dto.getParentCommentId() != null) {
            // parent 댓글
            Comment parentComment = existsCommentId(request.getParentCommentId());
            parentComment.addChildComment(newComment); // @todo check dirty-check
            return UpdateCountOfComment(newComment, post).toResponseDto();
        } else {
            return saveAndUpdateCountofComment(newComment, post).toResponseDto();
        }
    }

    public CommentDto.CommentResponse updateComment(long postId, long commentId, CommentDto.CommentRequest commentRequestDto) {
        // check
        CommonPost commonPost = existsPostId(postId);
        Comment comment = existsCommentId(commentId);

        // update
        comment.updateByRequestDto(commentRequestDto);
        return saveAndUpdateCountofComment(comment, commonPost).toResponseDto();
    }

    private Comment existsCommentId(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(InvalidTableIdException::new);
    }

    private CommonPost existsPostId(long postId) {
        try {

            return commonPostRepository.getReferenceById(postId);
        } catch (EntityNotFoundException e) {
            throw new InvalidTableIdException();
        }
    }

    private Comment UpdateCountOfComment(Comment newComment, CommonPost post) {
        post.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(post.getComments())
        );
        return newComment;
    }

    private Comment saveAndUpdateCountofComment(Comment comment, CommonPost post) {

        Comment saved = commentRepository.save(comment);
        post.updateCountOfComments(
                countOfReactionAndCommentApplyService.countComments(post.getComments())
        );
        return saved;
    }
}
