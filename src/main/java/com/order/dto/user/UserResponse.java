package com.order.dto.user;

public class UserResponse {
    private final boolean success;
    private final int status;
    private final String message;
    private final String token;
    private final Long userId;

    public UserResponse(boolean success, int status, String message, String token, Long userId) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.token = token;
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getUserId() {
        return userId;
    }
}
