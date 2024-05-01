package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class NotExistsSmallCategoryIdException extends CustomException {
    public NotExistsSmallCategoryIdException() {
        super(ErrorCode.NOT_EXISTS_SMALL_CATEGORY_ID_EXCEPTION);
    }
}
