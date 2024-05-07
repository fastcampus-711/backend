package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class InvalidURIException extends CustomException {
    public InvalidURIException() {
        super(ErrorCode.INVALID_URI_EXCEPTION);
    }
}
