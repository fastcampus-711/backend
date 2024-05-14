package com.aptner.v3.board.free_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class FreePost extends CommonPost {
    private LocalDateTime blindAt;
    private String blindBy;

    public FreePost() {}

    public FreePost(String title, String content, LocalDateTime blindAt, String blindBy) {
        super(title, content);
        this.blindAt = blindAt;
        this.blindBy = blindBy;
    }
}
