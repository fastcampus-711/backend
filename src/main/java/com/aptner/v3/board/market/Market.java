package com.aptner.v3.board.market;

import com.aptner.v3.board.common_post.domain.CommonPost;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("MarketPost")
public class Market extends CommonPost {
    private String type;
    private String status;

    private List<String> imageUrls;

    public Market(String title, String content, String type, String status) {
        super(title, content);
        this.type = type;
        this.status = status;
    }

    public Market(String title, String content, String type, String status, List<String> imageUrls) {
        super(title, content);
        this.type = type;
        this.status = status;
        this.imageUrls = imageUrls;
    }

    public Market() {

    }
}
