package com.aptner.v3.board.common_post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class CommonPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private LocalDateTime createdAt;
    private int hits;

    @Column(insertable = false, updatable = false)
    private String dtype;
    /** TODO
     *
     */
//    private String comments;
    private Boolean status = true;
}
