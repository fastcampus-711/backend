package com.aptner.v3.board.common.report.domain;

import com.aptner.v3.board.comment.Comment;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
@DiscriminatorValue("CommentReport")
public class CommentReport extends Report {

    @Setter
    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
