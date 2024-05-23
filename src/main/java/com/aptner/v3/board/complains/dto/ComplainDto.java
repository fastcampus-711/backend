package com.aptner.v3.board.complains.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ComplainDto extends CommonPostDto{
    @Getter
    public static class ComplainRequest extends CommonPostDto.CommonRequest {

    }

    @Getter
    @NoArgsConstructor
    public static class ComplainResponse extends CommonPostDto.CommonResponse {

    }
}