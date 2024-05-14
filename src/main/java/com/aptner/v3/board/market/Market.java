package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Market extends CommonPost {
    private String type;
    private String status;
}
