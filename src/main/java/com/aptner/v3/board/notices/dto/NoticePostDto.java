package com.aptner.v3.board.notices.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.notices.domain.NoticePost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.aptner.v3.CommunityApplication.modelMapper;

public class NoticePostDto extends CommonPostDto {

    @Getter
    public static class NoticeRequest extends CommonPostDto.CommonRequest {

        public NoticePost toEntity() {
            return modelMapper().map(this, NoticePost.class);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class NoticeResponse extends CommonPostDto.CommonResponse {

        public <E extends CommonPost> NoticeResponse(E entity) {
            super(entity);
        }
    }
}
