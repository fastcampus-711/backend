package com.aptner.v3.board.common.reaction;

import com.aptner.v3.board.common.reaction.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository<E extends Reaction> extends JpaRepository<E, Long> {
    // exists
    Optional<E> findByUserIdAndTargetIdAndDtype(long userId, long targetId, String postReaction);

    // post get
    Optional<List<E>> findByUserIdAndDtypeAndTargetId(long memberId, String commentReaction, long targetId);
}
