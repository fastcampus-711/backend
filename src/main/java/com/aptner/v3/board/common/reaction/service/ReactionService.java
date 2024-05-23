package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.Reaction;
import com.aptner.v3.board.common.reaction.dto.CountOfReactionTypeDto;
import com.aptner.v3.board.common.reaction.dto.ReactionDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ReactionService<T extends ReactionAndCommentCalculator, E extends Reaction> {
    private final CountOfReactionAndCommentApplyService<T> countOfReactionAndCommentApplyService;
    private final ReactionRepository<E> reactionRepository;

    protected ReactionService(JpaRepository<T, Long> jpaRepository,
                              ReactionRepository<E> reactionRepository) {
        this.countOfReactionAndCommentApplyService = new CountOfReactionAndCommentApplyService<>(jpaRepository);
        this.reactionRepository = reactionRepository;
    }

    public void acceptReaction(ReactionDto.Request reactionDto) {
        reactionRepository.findByUserIdAndTargetId(reactionDto.getUserId(), reactionDto.getTargetId())
                .ifPresentOrElse(reaction ->
                                reactionRepository.save(
                        (E)reaction.updateReactionType(reactionDto.getReactionType())),
                        () -> reactionRepository.save((E)reactionDto.toEntity())
                );

        applyReactionCount(reactionDto.getTargetId());
    }

    private void applyReactionCount(long targetId) {
        CountOfReactionTypeDto countOfReactionTypeDto = new CountOfReactionTypeDto(
                countAllByTargetIdAndReactionType(targetId, ReactionType.GOOD),
                countAllByTargetIdAndReactionType(targetId, ReactionType.BAD)
        );

        countOfReactionAndCommentApplyService.applyReactionCount(targetId, countOfReactionTypeDto);
    }

    private long countAllByTargetIdAndReactionType(long targetId, ReactionType reactionType) {
        return reactionRepository.countAllByTargetIdAndReactionType(targetId, reactionType);
    }
}
