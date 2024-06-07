package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final String subject;
    private final ErrorCode errorCode;

    public GlobalException(String message) {
        super(message);
        this.subject = null;
        this.errorCode = null;
    }

    public GlobalException(ErrorCode errorCode) {
        super();
        this.subject = null;
        this.errorCode = errorCode;
    }

    public GlobalException(ErrorCode errorCode, String subject) {
        super();
        this.subject = subject;
        this.errorCode = errorCode;
    }

    public GlobalException(ErrorCode errorCode, String msg, String subject) {
        super(msg);
        this.subject = subject;
        this.errorCode = errorCode;
    }
}
