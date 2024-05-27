package com.aptner.v3.global.util;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Map;

public class ResponseUtil {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, 200, null, data, System.currentTimeMillis());
    }

    public static ApiResponse<?> ok(SuccessCode successCode) {
        int statusCode = successCode.getHttpStatus().value();
        String msg = successCode.getDetail();
        return new ApiResponse<>(true, statusCode, msg, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> create(T data) {
        return new ApiResponse<>(true, 201, null, data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> update(T data) {
        return new ApiResponse<>(true, 201, null, data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> delete(T data) {
        return new ApiResponse<>(true, 204, null, data, System.currentTimeMillis());
    }

    public static ApiResponse<?> error(String Subject, ErrorCode errorCode) {
        String message = String.format(errorCode.getDetail(), Subject);
        return new ApiResponse<>(false
                , errorCode.getHttpStatus().value()
                , message
                , null
                , System.currentTimeMillis()
        );
    }

    public static ApiResponse<?> error(String Subject, ErrorCode errorCode, String error) {
        String msg = String.format(errorCode.getDetail(), Subject);
        return new ApiResponse<>(false
                , errorCode.getHttpStatus().value()
                , msg
                , error
                , System.currentTimeMillis());
    }

    public static ApiResponse<?> error(HttpStatusCode httpStatus, List<String> errors) {
        return new ApiResponse<>(false
                , httpStatus.value()
                , null
                , errors
                , System.currentTimeMillis());
    }
}
