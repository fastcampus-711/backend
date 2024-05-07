package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class InvalidTableIdException extends CustomException {
    public InvalidTableIdException() {
        super(ErrorCode.INVALID_TABLE_ID_EXCEPTION);
    }
}
