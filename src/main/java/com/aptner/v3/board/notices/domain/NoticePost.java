package com.aptner.v3.board.notices.domain;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("NOTICES")
public class NoticePost extends CommonPost {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postAt;

    public NoticePost() {
    }

    public NoticePost(String title, String content, LocalDateTime postAt) {
        super(title, content);
        this.postAt = postAt;
    }
}