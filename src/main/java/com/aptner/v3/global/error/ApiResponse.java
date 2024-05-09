package com.aptner.v3.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
    private T error;

    public ApiResponse(boolean success, int status, String message, T data, T error) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }

}
