package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class AlreadyExistsSmallCategoryNameException extends CustomException {
    public AlreadyExistsSmallCategoryNameException() {
        super(ErrorCode.ALREADY_EXISTS_SMALL_CATEGORY_NAME_EXCEPTION);
    }
}
