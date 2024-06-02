package com.aptner.v3.board.common.reaction.domain;

import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common.reaction.service.ReactionAndCommentCalculator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
public class  Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long targetId;

    private ReactionType reactionType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(updatable = false, insertable = false)
    private String dtype;

    public Reaction updateReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
        return this;
    }
}
