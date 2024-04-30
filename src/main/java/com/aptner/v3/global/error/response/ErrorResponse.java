package com.aptner.v3.global.error.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus httpStatus;
    private int code;
    private String message;
    private String detailMessage;
}
