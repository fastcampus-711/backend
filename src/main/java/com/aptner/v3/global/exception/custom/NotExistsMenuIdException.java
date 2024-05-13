package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;

public class NotExistsMenuIdException extends CustomException {
    public NotExistsMenuIdException() {
        super(ErrorCode.NOT_EXISTS_MENU_ID_EXCEPTION);
    }
}
