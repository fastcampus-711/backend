package com.aptner.v3.board.free_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("FreePost")
public class FreePost extends CommonPost {
    private String blindBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime blindAt;

    public FreePost() {}

    public FreePost(Member member, Category category, String title, String content, boolean visible, LocalDateTime blindAt, String blindBy) {
        super(member, category, title, content, visible);
        this.blindBy = blindBy;
        this.blindAt = blindAt;
    }

    public static FreePost of(Member member, Category category, String title, String content, boolean visible, LocalDateTime blindAt, String blindBy) {
        return new FreePost(member, category, title, content, visible, blindAt, blindBy);
    }
}
