package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentRepository;
import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.dto.ReactionDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final MemberRepository memberRepository;

    private final CommonPostRepository<CommonPost> postRepository;

    private final CommentRepository commentRepository;

    private final ReactionRepository reactionRepository;

    @Transactional
    public void savePostReaction(ReactionDto.ReactionRequest reactionDto) {

        Long userId = reactionDto.getUserId();
        Long postId = reactionDto.getTargetId();
        ReactionType reactionType = reactionDto.getReactionType();
        // check
        verifyMember(userId);
        CommonPost commonPost = verifyPost(postId);
        PostReaction reaction = (PostReaction) reactionRepository.findByUserIdAndTargetIdAndDtype(
                        userId, postId, "PostReaction")
                .orElse(new PostReaction());

        if (reaction.getId() == 0) {
            // New reaction
            reaction.setUserId(userId);
            reaction.setTargetId(postId);
            reaction.setPost(commonPost);

        } else {
            // Existing reaction, adjust counts
            adjustReactionCount(commonPost, reaction.getReactionType(), -1);
        }

        reaction.setReactionType(reactionType);
        reactionRepository.save(reaction);

        // Adjust new reaction count
        adjustReactionCount(commonPost, reactionType, 1);
        postRepository.save(commonPost);
    }

    @Transactional
    public void saveCommentReaction(ReactionDto.ReactionRequest reactionDto) {

        Long userId = reactionDto.getUserId();
        Long commentId = reactionDto.getTargetId();
        ReactionType reactionType = reactionDto.getReactionType();
        // check
        verifyMember(userId);
        Comment comment = verifyComment(reactionDto.getTargetId());
        CommentReaction reaction = (CommentReaction) reactionRepository.findByUserIdAndTargetIdAndDtype(
                        userId, commentId, "CommentReaction")
                .orElse(new CommentReaction());

        if (reaction.getId() == 0) {
            // New reaction
            reaction.setUserId(userId);
            reaction.setTargetId(commentId);
            reaction.setComment(comment);
        } else {
            // Existing reaction, adjust counts
            adjustReactionCount(comment, reaction.getReactionType(), -1);
        }

        reaction.setReactionType(reactionType);
        reactionRepository.save(reaction);

        // Adjust new reaction count
        adjustReactionCount(comment, reactionType, 1);
        commentRepository.save(comment);
    }

    private void adjustReactionCount(CommonPost post, ReactionType reactionType, int count) {
        ReactionColumns reactionColumns = post.getReactionColumns();
        if (reactionType == ReactionType.GOOD) {
            reactionColumns.setCountReactionTypeGood(reactionColumns.getCountReactionTypeGood() + count);
        } else if (reactionType == ReactionType.BAD) {
            reactionColumns.setCountReactionTypeBad(reactionColumns.getCountReactionTypeBad() + count);
        }
    }

    private void adjustReactionCount(Comment comment, ReactionType reactionType, int count) {
        ReactionColumns reactionColumns = comment.getReactionColumns();
        if (reactionType == ReactionType.GOOD) {
            reactionColumns.setCountReactionTypeGood(reactionColumns.getCountReactionTypeGood() + count);
        } else if (reactionType == ReactionType.BAD) {
            reactionColumns.setCountReactionTypeBad(reactionColumns.getCountReactionTypeBad() + count);
        }
    }

    private CommonPost verifyPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    private Member verifyMember(Long userId) {

        return memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Comment verifyComment(long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
}
