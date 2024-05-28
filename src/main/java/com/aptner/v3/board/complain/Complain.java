package com.aptner.v3.board.complain;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("ComplaintPost")
public class Complain extends CommonPost {

    public Complain() {
    }

    public Complain(String title, String content) {
        super(title, content);
    }
}
