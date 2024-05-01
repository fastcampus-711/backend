package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class NotExistsBigCategoryIdException extends CustomException {
    public NotExistsBigCategoryIdException() {
        super(ErrorCode.NOT_EXISTS_BIG_CATEGORY_ID_EXCEPTION);
    }
}
