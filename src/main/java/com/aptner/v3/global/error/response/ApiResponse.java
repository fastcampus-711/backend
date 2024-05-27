package com.aptner.v3.global.error.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "status", "message", "timestamp"})
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
    private long timestamp;

    public ApiResponse(boolean success, int status, String message, T data, long timestamp) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

}
