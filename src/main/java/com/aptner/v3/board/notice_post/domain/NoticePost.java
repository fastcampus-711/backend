package com.aptner.v3.board.notice_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("NoticePost")
public class NoticePost extends CommonPost {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime postAt;

    public NoticePost() {
    }

    public NoticePost(Member member, Category category, String title, String content, boolean visible, LocalDateTime postAt) {
        super(member, category, title, content, visible);
        this.postAt = postAt;
    }

    public static NoticePost of(Member member, Category category, String title, String content, boolean visible, LocalDateTime postAt) {
        return new NoticePost(member, category, title, content, visible, postAt);
    }
}