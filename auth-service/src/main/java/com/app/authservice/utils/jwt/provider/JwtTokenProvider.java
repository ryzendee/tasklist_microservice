package com.app.authservice.utils.jwt.provider;

import com.app.authservice.models.TokenData;

public interface JwtTokenProvider {
    String generateRefreshToken(TokenData tokenData);
    String generateAccessToken(TokenData tokenData);
    String refreshAccessToken(String refreshToken);
    boolean isTokenValid(String token);
}
