package com.aptner.v3.board.markets;

import com.aptner.v3.board.commons.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("MARKETS")
public class Market extends CommonPost {
    private String type;
    private String status;
}
