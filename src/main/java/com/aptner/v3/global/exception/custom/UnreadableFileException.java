package com.aptner.v3.global.exception.custom;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CustomException;

public class UnreadableFileException extends CustomException {
    public UnreadableFileException() {
        super(ErrorCode.UNREADABLE_FILE_EXCEPTION);
    }
}
