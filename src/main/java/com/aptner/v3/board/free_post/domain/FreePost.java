package com.aptner.v3.board.free_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class FreePost extends CommonPost {
    private String blindBy;
    private LocalDateTime blindAt;

    private List<String> imageUrls;


    public FreePost() {}

    public FreePost(String title, String content, LocalDateTime blindAt, String blindBy) {
        super(title, content);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
    }
    public FreePost(String title, String content, LocalDateTime blindAt, String blindBy, List<String> imageUrls) {
        super(title, content);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
        this.imageUrls = imageUrls;
    }
}
