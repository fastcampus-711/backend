package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;

public class CommentException extends GlobalException {

    public CommentException(ErrorCode responseCode) {
        super(responseCode, "댓글");
    }

    public CommentException(String message) {
        super(message);
    }

}