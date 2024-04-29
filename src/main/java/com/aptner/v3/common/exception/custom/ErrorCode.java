package com.aptner.v3.common.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    BINDING_EXCEPTION(HttpStatus.BAD_REQUEST, "binding exception"),

    ALREADY_REGISTERED_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "Already Registered Username");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
