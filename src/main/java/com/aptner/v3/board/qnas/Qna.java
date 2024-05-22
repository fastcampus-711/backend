package com.aptner.v3.board.qnas;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Qna extends CommonPost {
    private String type;
    private String status;
}
