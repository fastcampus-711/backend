package com.aptner.v3.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_EXISTS_BIG_CATEGORY_ID_EXCEPTION(HttpStatus.NOT_FOUND, "not existed big category id"),
    NOT_EXISTS_SMALL_CATEGORY_ID_EXCEPTION(HttpStatus.NOT_FOUND, "not existed small category id"),
    ALREADY_EXISTS_SMALL_CATEGORY_NAME_EXCEPTION(HttpStatus.CONFLICT, "already existed small category name"),

    BINDING_EXCEPTION(HttpStatus.BAD_REQUEST, "binding exception"),
    ALREADY_REGISTERED_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "Already Registered Username");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
