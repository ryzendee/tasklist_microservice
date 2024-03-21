package com.app.authservice.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
