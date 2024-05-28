package com.aptner.v3.board.market.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.market.Market;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class MarketDto extends CommonPostDto {

    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class MarketReqeust extends CommonPostDto.CommonPostRequest {
        private String type;
        private String status;

        public MarketReqeust(Long id, Long categoryId, String title, String content, Boolean visible, List<String> imageUrls, String type, String status) {
            super(id, categoryId, title, content, visible, imageUrls);
            this.type = type;
            this.status = status;
        }

        public Market toEntity() {
            return new Market(getTitle(), getContent(), type, status);
        }

        public Market toEntity(List<String> imageUrls) {
            return new Market(getTitle(), getContent(), type, status, imageUrls);
        }
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor
    public static class MarketResponse extends CommonPostDto.CommonPostResponse {
        private long id;
        private String type;
        private String status;
        private List<String> imageUrls;

        public <E extends CommonPost> MarketResponse(E entity) {
            super(entity);
        }
    }
}
