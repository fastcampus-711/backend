package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class NotExistsCategoryIdException extends CustomException {
    public NotExistsCategoryIdException() {
        super(ErrorCode.NOT_EXISTS_CATEGORY_ID_EXCEPTION);
    }
}
