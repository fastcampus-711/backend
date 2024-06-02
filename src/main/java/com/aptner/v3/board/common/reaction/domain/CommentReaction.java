package com.aptner.v3.board.common.reaction.domain;

import com.aptner.v3.board.comment.Comment;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
@DiscriminatorValue("CommentReaction")
public class CommentReaction extends Reaction {

    @Setter
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
