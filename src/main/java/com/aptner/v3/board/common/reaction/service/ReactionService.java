package com.aptner.v3.board.common.reaction.service;

import com.aptner.v3.board.common.reaction.ReactionRepository;
import com.aptner.v3.board.common.reaction.domain.Reaction;
import com.aptner.v3.board.common.reaction.dto.ReactionDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class ReactionService<T extends ReactionColumns, E extends Reaction> {
    private final CountOfReactionAndCommentApplyService<T> countOfReactionAndCommentApplyService;
    private final ReactionRepository<E> reactionRepository;

    protected ReactionService(CountOfReactionAndCommentApplyService<T> countOfReactionAndCommentApplyService,
                              ReactionRepository<E> reactionRepository) {
        this.countOfReactionAndCommentApplyService = countOfReactionAndCommentApplyService;
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
        long countReactionTypeGood = countAllByTargetIdAndReactionType(targetId, ReactionType.GOOD);
        long countReactionTypeBad = countAllByTargetIdAndReactionType(targetId, ReactionType.BAD);

        countOfReactionAndCommentApplyService.applyReactionCount(targetId, countReactionTypeGood, countReactionTypeBad);
    }

    private long countAllByTargetIdAndReactionType(long targetId, ReactionType reactionType) {
        return reactionRepository.countAllByTargetIdAndReactionType(targetId, reactionType);
    }
}
