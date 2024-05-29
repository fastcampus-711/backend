package com.aptner.v3.board.complain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("ComplaintPost")
public class Complain extends CommonPost {

    public Complain() {
    }

    public Complain(Member member, Category category, String title, String content, boolean visible) {
        super(member, category, title, content, visible);
    }

    public static Complain of(Member member, Category category, String title, String content, boolean visible) {
        return new Complain(member, category, title, content, visible);
    }
}
