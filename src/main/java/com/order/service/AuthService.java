package com.order.service;

import com.order.dto.auth.LoginRequest;
import com.order.dto.auth.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthService {
    private final RestTemplate restTemplate;
    private static final String AUTH_URL = "http://localhost:8080/users/login";
    private String cachedToken;

    @Autowired
    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        if (cachedToken == null) {
            cachedToken = authenticate();
        }
        return cachedToken;
    }

    private String authenticate() {
        LoginRequest loginRequest = new LoginRequest("admin", "admin"); // Replace with actual credentials

        try {
            LoginResponse response = restTemplate.postForObject(AUTH_URL, loginRequest, LoginResponse.class);
            return Objects.requireNonNull(response).getToken();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
