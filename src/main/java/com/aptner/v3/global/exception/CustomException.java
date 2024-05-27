package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends GlobalException {

    public CustomException(ErrorCode responseCode) {
        super(responseCode);
    }

    public CustomException(String message) {
        super(message);
    }

}