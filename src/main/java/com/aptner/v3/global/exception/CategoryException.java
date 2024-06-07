package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;

public class CategoryException  extends GlobalException {

    public CategoryException(ErrorCode responseCode) {
        super(responseCode, "분류");
    }

}