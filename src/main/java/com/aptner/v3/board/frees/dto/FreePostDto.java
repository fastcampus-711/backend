package com.aptner.v3.board.frees.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.frees.domain.FreePost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.aptner.v3.CommunityApplication.modelMapper;

@AllArgsConstructor
public class FreePostDto extends CommonPostDto {

    @Getter
    public static class FreeCommonRequest extends CommonPostDto.CommonRequest {
        public FreePost toEntity() {
            return modelMapper().map(this, FreePost.class);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class FreeCommonResponse extends CommonPostDto.CommonResponse {
        private LocalDateTime blindAt;
        private String blindBy;
        private List<String> imageUrls;

        public <E extends CommonPost> FreeCommonResponse(E entity) {
            super(entity);
        }
    }
}
