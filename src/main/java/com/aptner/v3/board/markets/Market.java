package com.aptner.v3.board.markets;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Market extends CommonPost {
    private String type;
    private String status;
}
