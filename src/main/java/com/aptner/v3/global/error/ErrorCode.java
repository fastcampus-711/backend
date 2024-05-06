package com.aptner.v3.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_EXISTS_MENU_ID_EXCEPTION(HttpStatus.NOT_FOUND, "not existed menu id"),
    NOT_EXISTS_CATEGORY_ID_EXCEPTION(HttpStatus.NOT_FOUND, "not existed category id"),
    ALREADY_EXISTS_CATEGORY_NAME_EXCEPTION(HttpStatus.CONFLICT, "already existed category name"),
    INVALID_URI_EXCEPTION(HttpStatus.BAD_REQUEST, "Invalid URI"),

    BINDING_EXCEPTION(HttpStatus.BAD_REQUEST, "binding exception"),
    ALREADY_REGISTERED_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "Already Registered Username");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
