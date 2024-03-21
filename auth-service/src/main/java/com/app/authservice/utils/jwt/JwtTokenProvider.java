package com.app.authservice.utils.jwt;

import com.app.authservice.enums.ClaimsKey;
import com.app.authservice.enums.Role;

public interface JwtTokenProvider {
    String generateRefreshToken(String subject, Role role);
    String generateAccessToken(String subject, Role role);
    String getClaimsData(ClaimsKey key);
    boolean isTokenValid(String token);
}
