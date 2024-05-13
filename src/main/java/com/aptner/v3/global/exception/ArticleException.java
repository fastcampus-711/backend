package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ArticleException extends GlobalException {

    public ArticleException(ErrorCode responseCode) {
        super("게시글", responseCode);
    }

}