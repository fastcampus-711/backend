package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AuthException extends GlobalException {
    public AuthException(ErrorCode responseCode) {
        super("권한", responseCode);
    }
}
