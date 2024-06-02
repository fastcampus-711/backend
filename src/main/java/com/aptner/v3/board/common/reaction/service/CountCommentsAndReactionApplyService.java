package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.dto.CountOfReactionTypeDto;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.ReactionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CountCommentsAndReactionApplyService<E extends ReactionAndCommentCalculator> {
    private final JpaRepository<E, Long> jpaRepository;

    public CountCommentsAndReactionApplyService(JpaRepository<E, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    protected void applyReactionCount(long targetId, CountOfReactionTypeDto countOfReactionTypeDto) {
        jpaRepository.findById(targetId)
                .orElseThrow(() -> new ReactionException(ErrorCode.TARGET_NOT_FOUND))
                .updateCountOfReactions(countOfReactionTypeDto);
    }
}
