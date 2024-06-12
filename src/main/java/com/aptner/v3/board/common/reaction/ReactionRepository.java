package com.aptner.v3.board.common.reaction;

import com.aptner.v3.board.common.reaction.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository<E extends Reaction> extends JpaRepository<E, Long> {
    // exists
    Optional<E> findByUserIdAndTargetIdAndDtype(long userId, long targetId, String postReaction);

    // post get
    @Query("SELECT c FROM CommentReaction c WHERE c.comment.commonPost.id = :postId And c.dtype = :commentReaction")
    Optional<List<E>> findByDtypeAndTargetId(String commentReaction, long postId);
}
