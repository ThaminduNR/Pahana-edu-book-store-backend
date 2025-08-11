package com.pahanaedu.util;

public class ApiResponse<T> {
    private String message;
    private int status;
    private T data;
    private boolean success;

    public ApiResponse(String message, int status, T data, boolean success) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
