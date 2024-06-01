package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class MenuException extends GlobalException {

    public MenuException(ErrorCode responseCode) {
        super(responseCode, "메뉴");
    }

}