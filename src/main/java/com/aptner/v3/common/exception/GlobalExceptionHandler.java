package com.aptner.v3.common.exception;

import com.aptner.v3.common.exception.custom.CustomException;
import com.aptner.v3.common.exception.custom.ErrorCode;
import com.aptner.v3.common.exception.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return handleInternalException(e.getErrorCode());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleInternalException(ErrorCode.BINDING_EXCEPTION, ex.getMessage());
    }

    private ResponseEntity<Object> handleInternalException(ErrorCode errorCode) {
        return new ResponseEntity<>(makeErrorResponse(errorCode), errorCode.getHttpStatus());
    }

    private ResponseEntity<Object> handleInternalException(ErrorCode errorCode, String detailMessage) {
        return new ResponseEntity<>(makeErrorResponse(errorCode, detailMessage), errorCode.getHttpStatus());
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .httpStatus(errorCode.getHttpStatus())
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String detailMessage) {
        return ErrorResponse.builder()
                .httpStatus(errorCode.getHttpStatus())
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .detailMessage(detailMessage)
                .build();
    }
}
