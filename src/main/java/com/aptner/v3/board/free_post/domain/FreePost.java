package com.aptner.v3.board.free_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
public class FreePost extends CommonPost {
    private String blindBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime blindAt;

    public FreePost() {}

    public FreePost(String title, String content, LocalDateTime blindAt, String blindBy) {
        super(title, content);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
    }
}
