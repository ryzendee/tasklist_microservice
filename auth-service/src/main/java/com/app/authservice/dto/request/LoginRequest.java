package com.app.authservice.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
