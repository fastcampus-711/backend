package com.aptner.v3.reaction;

import com.aptner.v3.reaction.domain.Reaction;
import com.aptner.v3.reaction.dto.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository<E extends Reaction> extends JpaRepository<E, Long> {
    Optional<E> findByUserIdAndTargetId(long userId, long targetId);

    long countAllByTargetIdAndReactionType(long targetId, ReactionType reactionType);
}
