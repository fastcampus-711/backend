package com.aptner.v3.global.exception;

import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ApiResponse<?> handleAuthException(GlobalException e) {
        return ResponseUtil.error(e.getSubject(), e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ApiResponse<?> handleUserException(UserException e) {
        return ResponseUtil.error(e.getSubject(), e.getErrorCode());
    }

    @ExceptionHandler(MenuException.class)
    public ApiResponse<?> handleMenuException(MenuException e) {
        return ResponseUtil.error(e.getSubject(), e.getErrorCode());
    }

    @ExceptionHandler(CategoryException.class)
    public ApiResponse<?> handleCategoryException(CategoryException e) {
        return ResponseUtil.error(e.getSubject(), e.getErrorCode());
    }

    @ExceptionHandler(PostException.class)
    public ApiResponse<?> handlePostException(PostException e) {
        return ResponseUtil.error(e.getSubject(), e.getErrorCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ApiResponse<?> handleAccessDeniedException(final AccessDeniedException e) {
        return ResponseUtil.error("권한", ErrorCode.INVALID_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> handleCustomException(CustomException e) {
        return ResponseUtil.error(e.getSubject(), e.getErrorCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("success", false);
        data.put("timestamp", System.currentTimeMillis());
        data.put("status", status.value());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getDefaultMessage()).collect(Collectors.toList());
        data.put("errors", errors);
        return new ResponseEntity<Object>(data, status);
    }
}
