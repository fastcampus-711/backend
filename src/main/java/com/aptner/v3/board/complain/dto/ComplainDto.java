package com.aptner.v3.board.complain.dto;

import com.aptner.v3.board.common_post.CommonPostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ComplainDto extends CommonPostDto {
    @Getter
    public static class Request extends CommonPostDto.Request {

    }

    @Getter
    @NoArgsConstructor
    public static class Response extends CommonPostDto.Response {

    }
}
