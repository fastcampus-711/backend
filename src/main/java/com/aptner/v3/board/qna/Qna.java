package com.aptner.v3.board.qna;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("QnaPost")
public class Qna extends CommonPost {
    private String type;
    private String status;

    public Qna() {}

    public Qna(Member member, Category category, String title, String content, boolean visible, String type, String status) {
        super(member, category, title, content, visible);
        this.type = type;
        this.status = status;
    }

    public static Qna of(Member member, Category category, String title, String content, boolean visible, String type, String status) {
        return new Qna(member, category, title, content, visible, type, status);
    }
}
