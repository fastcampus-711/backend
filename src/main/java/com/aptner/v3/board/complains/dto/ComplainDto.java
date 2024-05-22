package com.aptner.v3.board.complains.dto;

import com.aptner.v3.board.commons.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ComplainDto {
    @Getter
    public static class Request extends CommonPostDto.Request {

    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {

    }
}
