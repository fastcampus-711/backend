package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.ReactionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ReactionApplyService<E extends ReactionColumns> {
    private final JpaRepository<E, Long> jpaRepository;

    protected ReactionApplyService(JpaRepository<E, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public void applyReactionCount(long targetId, long countReactionTypeGood, long countReactionTypeBad) {
        jpaRepository.findById(targetId)
                .orElseThrow(() -> new ReactionException(ErrorCode.TARGET_NOT_FOUND))
                .updateCountOfReactions(countReactionTypeGood, countReactionTypeBad);
    }
}
