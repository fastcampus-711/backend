package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CustomException;

public class InValidHouseIdException extends CustomException {
    public InValidHouseIdException() {
        super(ErrorCode.INVALID_HOUSE_ID_EXCEPTION);
    }
}
