package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class AlreadyRegisteredUserException extends CustomException {
    public AlreadyRegisteredUserException() {
        super(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
    }
}
