package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PostException extends GlobalException {

    public PostException(String message) {
        super(message);
    }

    public PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PostException(String subject, ErrorCode errorCode) {
        super(subject, errorCode);
    }

    public PostException(String subject, ErrorCode errorCode, String msg) {
        super(subject, errorCode, msg);
    }
}