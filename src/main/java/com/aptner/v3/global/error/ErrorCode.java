package com.aptner.v3.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    // 500
    S3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "[%] S3 오류가 발생하였습니다."),
    // 400 Bad Request
    INVALID_REQUEST(BAD_REQUEST, "잘못된 입력입니다."),
    INCORRECT_CODE(BAD_REQUEST, "잘못된 인증번호입니다."),
    EMAIL_REQUIRED(BAD_REQUEST, "이메일을 입력해 주세요."),
    INVALID_PASSWORD_FORMAT(BAD_REQUEST, "비밀번호는 대소문자 영문과 숫자 조합으로 8자 이상 15자 이하로 입력해주세요."),

    // 409 Conflict
    DUPLICATE_RESOURCE(CONFLICT, "중복된 이메일 또는 닉네임입니다."),
    DUPLICATE_NICKNAME(CONFLICT, "중복된 닉네임입니다."),
    DUPLICATE_EMAIL(CONFLICT, "중복된 이메일입니다."),
    _ALREADY_EXIST(CONFLICT, "중복된 %s입니다."),

    // 404 NOT_FONUD
    _NOT_FOUND(NOT_FOUND, "%s 정보를 찾을 수 없습니다."),
    _EMPTY_FILE(NOT_FOUND, "파일이 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    ARTICLE_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    NOT_AVAILABLE_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NOT_MATCHED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰의 유저 정보가 일치하지 않습니다."),

    //  500
    TOKEN_CREATION_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "토큰을 생성하는 과정에서 알 수 없는 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    NOT_EXISTS_MENU_ID_EXCEPTION(HttpStatus.NOT_FOUND, "not existed menu id"),
    NOT_EXISTS_CATEGORY_ID_EXCEPTION(HttpStatus.NOT_FOUND, "not existed category id"),
    ALREADY_EXISTS_CATEGORY_NAME_EXCEPTION(HttpStatus.CONFLICT, "already existed category name"),
    INVALID_URI_EXCEPTION(HttpStatus.BAD_REQUEST, "Invalid URI"),
    INVALID_TABLE_ID_EXCEPTION(HttpStatus.BAD_REQUEST, "Invalid table id exception"),
    BINDING_EXCEPTION(HttpStatus.BAD_REQUEST, "binding exception"),
    ALREADY_REGISTERED_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "Already Registered Username"),

    PASSWORD_MISMATCH_EXCEPTION(HttpStatus.BAD_REQUEST, "Password Mismatch");

    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
