package com.aptner.v3.board.common.report.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
@DiscriminatorValue("PostReport")
public class PostReport extends Report {

    @Setter
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private CommonPost post;
}
