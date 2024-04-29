package com.aptner.v3.common.exception.response;

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
