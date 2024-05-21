package com.aptner.v3.global.util;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ResponseUtil {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, 200, null, data, null);
    }

    public static ApiResponse<?> ok(SuccessCode successCode) {
        int statusCode = successCode.getHttpStatus().value();
        String msg = successCode.getDetail();
        return new ApiResponse<>(true, statusCode, msg, null, null);
    }

    public static <T> ApiResponse<T> create(T data) {
        return new ApiResponse<>(true, 201, null, data, null);
    }
    public static <T> ApiResponse<T> update(T data) {
        return new ApiResponse<>(true, 201, null, data, null);
    }
    public static <T> ApiResponse<T> delete(T data) {
        return new ApiResponse<>(true, 204, null, data, null);
    }

    public static ApiResponse<?> error(String Subject, ErrorCode errorCode) {
        int statusCode = errorCode.getHttpStatus().value();
        String msg = String.format(errorCode.getDetail(), Subject);
        return new ApiResponse<>(false, statusCode, msg, null, null);
    }

    public static ApiResponse<?> error(String Subject, ErrorCode errorCode, String message) {
        int statusCode = errorCode.getHttpStatus().value();
        String msg = String.format(errorCode.getDetail(), Subject);
        return new ApiResponse<>(false, statusCode, msg, null, message);
    }

    public static ApiResponse<?> error(HttpStatus httpStatus, Map<String, String> errors) {
        return new ApiResponse<>(false, httpStatus.value(), null, null, errors);
    }
}
