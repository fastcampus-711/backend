package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;

public class AttachException extends GlobalException {

    public AttachException(ErrorCode responseCode) {
        super(responseCode, "첨부 파일");
    }

}
