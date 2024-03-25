package com.app.authservice.service.jwt;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.TokenType;
import com.app.authservice.exception.custom.GenerateTokenException;
import com.app.authservice.mapper.interfaces.AuthUserMapToTokenData;
import com.app.authservice.models.TokenData;
import com.app.authservice.utils.jwt.provider.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthUserMapToTokenData authUserMapToTokenData;

    @Override
    public String generateAccessToken(AuthUser authUser) throws GenerateTokenException {
        return generateToken(authUser, TokenType.ACCESS);
    }

    @Override
    public String generateRefreshToken(AuthUser authUser) throws GenerateTokenException {
        return generateToken(authUser, TokenType.REFRESH);
    }

    private String generateToken(AuthUser authUser, TokenType tokenType) {
        TokenData tokenData = authUserMapToTokenData.map(authUser);
        try {
            return tokenType.equals(TokenType.REFRESH)
                    ? jwtTokenProvider.generateRefreshToken(tokenData)
                    : jwtTokenProvider.generateAccessToken(tokenData);
        } catch (JwtException ex) {
            log.error("Failed to generate token: {}", ex.getMessage());
            throw new GenerateTokenException("Failed to generate token");
        }
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        try {
            return jwtTokenProvider.refreshAccessToken(refreshToken);
        } catch (JwtException ex) {
            log.error("Failed to refresh access token: {}", ex.getMessage());
            throw new GenerateTokenException("Failed to refresh access token");
        }
    }

    @Override
    public void retrieveRefreshToken(String refreshToken) {
        //TODO
    }

    @Override
    public boolean isTokenValid(String token) {
        return jwtTokenProvider.isTokenValid(token);
    }
}
