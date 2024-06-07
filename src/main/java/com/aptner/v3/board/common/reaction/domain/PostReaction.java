package com.aptner.v3.board.common.reaction.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
@DiscriminatorValue("PostReaction")
public class PostReaction extends Reaction {

    @Setter
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private CommonPost post;
}
