package com.aptner.v3.board.common.reaction;

import com.aptner.v3.board.common.reaction.domain.Reaction;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository<E extends Reaction> extends JpaRepository<E, Long> {
    Optional<E> findByUserIdAndTargetIdAndDtype(long userId, long targetId, String postReaction);

    long countAllByTargetIdAndReactionType(long targetId, ReactionType reactionType);

    List<E> findByUserIdAndDtype(long memberId, String commentReaction);
}
