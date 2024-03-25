package com.app.authservice.service.jwt;

import com.app.authservice.entity.AuthUser;

public interface JwtService {

    String generateAccessToken(AuthUser authUser);
    String generateRefreshToken(AuthUser authUser);
    String refreshAccessToken(String refreshToken);
    void retrieveRefreshToken(String refreshToken);
    boolean isTokenValid(String token);
}
