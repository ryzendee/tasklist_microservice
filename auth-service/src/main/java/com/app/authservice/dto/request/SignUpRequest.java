package com.app.authservice.dto.request;

public record SignUpRequest(
        String email,
        String password,
        String passwordConfirmation
) {
}
