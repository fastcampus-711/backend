package com.aptner.v3.board.notice_post.domain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
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
