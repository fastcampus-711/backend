package com.aptner.v3.board.common.reaction.dto;

import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import lombok.Getter;

@Getter
public enum ReactionTarget {
    POST(PostReaction.class), COMMENT(CommentReaction.class);

    private final Class<?> reactionClazz;

    ReactionTarget(Class<?> reactionClazz) {
        this.reactionClazz = reactionClazz;
    }
}
