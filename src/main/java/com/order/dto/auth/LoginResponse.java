package com.order.dto.auth;



public class LoginResponse {
    private boolean success;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
