package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PostException extends GlobalException {

    public PostException(ErrorCode responseCode) {
        super(responseCode, "게시글");
    }

    public PostException(String message) {
        super(message);
    }
}