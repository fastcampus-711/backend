package com.aptner.v3.reaction.domain;

import com.aptner.v3.reaction.dto.ReactionType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long targetId;

    private ReactionType reactionType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Reaction updateReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
        return this;
    }
}
