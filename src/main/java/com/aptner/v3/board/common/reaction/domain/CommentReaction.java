package com.aptner.v3.board.common.reaction.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CommentReaction")
public class CommentReaction extends Reaction {
}
