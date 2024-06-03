package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CustomException;

public class InValidUserInfoException extends CustomException {
    public InValidUserInfoException() {
        super(ErrorCode.INVALID_USER_INFO_EXCEPTION);
    }
}
