package com.aptner.v3.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@Getter
public enum SuccessCode {

    // 200 OK

    // 201 Created
    USER_SIGNUP_SUCCESS(CREATED, "회원가입 성공"),
    POST_CREATE_SUCCESS(CREATED, "게시글이 등록되었습니다."),
    COMMENT_CREATE_SUCCESS(CREATED, "댓글이 등록되었습니다."),
    REACTION_APPLY_SUCCESS(ACCEPTED, "반응이 반영되었습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String detail;

    SuccessCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
