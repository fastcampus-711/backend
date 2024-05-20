package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.ReactionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public abstract class CountOfReactionAndCommentApplyService<E extends ReactionColumns> {
    private final JpaRepository<E, Long> jpaRepository;

    protected CountOfReactionAndCommentApplyService(JpaRepository<E, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    protected void applyReactionCount(long targetId, long countReactionTypeGood, long countReactionTypeBad) {
        jpaRepository.findById(targetId)
                .orElseThrow(() -> new ReactionException(ErrorCode.TARGET_NOT_FOUND))
                .updateCountOfReactions(countReactionTypeGood, countReactionTypeBad);
    }

    protected long countComments(List<Comment> comments) {
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
