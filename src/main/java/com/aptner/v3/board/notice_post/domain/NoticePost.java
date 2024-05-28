package com.aptner.v3.board.notice_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("NoticePost")
public class NoticePost extends CommonPost {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postAt = LocalDateTime.now();

    private List<String> imageUrls;

    public NoticePost() {
    }

    public NoticePost(String title, String content, LocalDateTime postAt) {
        super(title, content);
        this.postAt = postAt;
    }

    public NoticePost(String title, String content, LocalDateTime postAt, List<String> imageUrls) {
        super(title, content);
        this.postAt = postAt;
        this.imageUrls = imageUrls;
    }
}