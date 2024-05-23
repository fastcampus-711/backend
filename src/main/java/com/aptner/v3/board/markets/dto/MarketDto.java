package com.aptner.v3.board.markets.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.markets.MarketPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.aptner.v3.CommunityApplication.modelMapper;

public class MarketDto extends CommonPostDto{
    @Getter
    public static class MarketRequest extends CommonPostDto.CommonRequest {
        private String type;
        private String status;

        public MarketPost toEntity() {
            return modelMapper().map(this, MarketPost.class);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MarketResponse extends CommonPostDto.CommonResponse {
        private long id;
        private String type;
        private String status;

        public <E extends CommonPost> MarketResponse(E entity) {
            super(entity);
        }
    }
}
