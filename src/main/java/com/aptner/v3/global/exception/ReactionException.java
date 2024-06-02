package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;

public class ReactionException extends GlobalException{
    public ReactionException(ErrorCode responseCode) {
        super(responseCode, "좋아요");
    }
}
