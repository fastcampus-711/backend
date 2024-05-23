package com.aptner.v3.board.market.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.market.Market;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MarketDto {
    @Getter
    public static class Request extends CommonPostDto.Request {
        private String type;
        private String status;
        private List<String> imageUrls;

        public Market toEntity() {
            return new Market(getTitle(), getContent(), type, status);
        }

        public Market toEntity(List<String> imageUrls) {
            return new Market(getTitle(), getContent(), type, status, imageUrls);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {
        private long id;
        private String type;
        private String status;
        private List<String> imageUrls;

        public <E extends CommonPost> Response(E entity) {
            super(entity);
        }
    }
}
