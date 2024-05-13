package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class AlreadyExistsCategoryNameException extends CustomException {
    public AlreadyExistsCategoryNameException() {
        super(ErrorCode.ALREADY_EXISTS_CATEGORY_NAME_EXCEPTION);
    }
}
