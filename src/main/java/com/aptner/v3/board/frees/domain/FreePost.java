package com.aptner.v3.board.frees.domain;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class FreePost extends CommonPost {
    private String blindBy;
    private LocalDateTime blindAt;

    public FreePost() {}

    public FreePost(String title, String content, LocalDateTime blindAt, String blindBy) {
        super(title, content);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
    }
}
