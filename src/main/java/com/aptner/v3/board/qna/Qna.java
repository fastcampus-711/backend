package com.aptner.v3.board.qna;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("QnaPost")
public class Qna extends CommonPost {
    private String type;
    private String status;
}
