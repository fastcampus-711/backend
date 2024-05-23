package com.aptner.v3.board.qnas.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
public class QnaDto extends CommonPostDto {
    @Getter
    @SuperBuilder
    public static class QnaRequest extends CommonPostDto.CommonRequest {
        private String type;
        private String status;
    }

    @Getter
    @NoArgsConstructor
    public static class QnaResponse extends CommonPostDto.CommonResponse {
        private long id;
        private String type;
        private String status;
    }
}
