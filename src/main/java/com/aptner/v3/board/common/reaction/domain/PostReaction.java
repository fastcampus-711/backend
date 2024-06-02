package com.aptner.v3.board.common.reaction.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PostReaction")
public class PostReaction extends Reaction {
}
