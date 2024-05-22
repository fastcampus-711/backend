package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentRepository;
import com.aptner.v3.board.common.reaction.dto.CountOfReactionTypeDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.ReactionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CountOfReactionAndCommentApplyService<E extends ReactionAndCommentCalculator> {
    private final JpaRepository<E, Long> jpaRepository;

    public CountOfReactionAndCommentApplyService(JpaRepository<E, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    protected void applyReactionCount(long targetId, CountOfReactionTypeDto countOfReactionTypeDto) {
        jpaRepository.findById(targetId)
                .orElseThrow(() -> new ReactionException(ErrorCode.TARGET_NOT_FOUND))
                .updateCountOfReactions(countOfReactionTypeDto);
    }

    public long countComments(List<Comment> comments) {
        if (comments == null)
            return 0;

        long count = 0;
        for (Comment comment : comments) {
            count++;
            count += countComments(comment.getChildComments());
        }
        return count;
    }
}
