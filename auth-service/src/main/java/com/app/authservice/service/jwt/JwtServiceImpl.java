package com.app.authservice.service.jwt;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.TokenType;
import com.app.authservice.exception.custom.GenerateTokenException;
import com.app.authservice.repository.token.TokenRepository;
import com.app.authservice.utils.jwt.factory.JwtFactory;
import com.app.authservice.utils.jwt.generator.JwtClaimsCreator;
import com.app.authservice.utils.jwt.parser.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final TokenRepository tokenRepository;

    private final JwtFactory jwtFactory;
    private final JwtParser jwtParser;
    private final JwtClaimsCreator jwtClaimsCreator;

    @Override
    public String generateAccessToken(AuthUser authUser) throws GenerateTokenException {
        return generateToken(authUser, TokenType.ACCESS);
    }

    @Override
    public String generateRefreshToken(AuthUser authUser) throws GenerateTokenException {
        return generateToken(authUser, TokenType.REFRESH);
    }

    private String generateToken(AuthUser authUser, TokenType tokenType) {
        validateUser(authUser);
        Claims claims = jwtClaimsCreator.getTokenClaims(authUser, tokenType);
        return jwtFactory.createToken(claims);
    }


    private void validateUser(AuthUser authUser) {
        if (authUser.getId() == null || authUser.getRole() == null) {
            log.error("Invalid user credentials: id={}, role={}", authUser.getId(), authUser.getRole());
            throw new GenerateTokenException("Invalid user credentials");
        }
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        validateToken(refreshToken);

        Claims claimsFromRefreshToken = jwtParser.parseToken(refreshToken);
        Claims updatedClaims = jwtClaimsCreator.refreshClaimsForAccessToken(claimsFromRefreshToken);

        return jwtFactory.createToken(updatedClaims);
    }

    @Override
    public void retrieveRefreshToken(String refreshToken) {
        validateToken(refreshToken);
        Claims claims = jwtParser.parseToken(refreshToken);
        tokenRepository.saveTokenWithExpiration(claims.getId(), refreshToken, claims.getExpiration().getTime());
        log.info("Saved token jti: {}", tokenRepository.findTokenById(claims.getId()));
    }

    private void validateToken(String token) {
        if (isTokenValid(token) || isTokenRetrieved(token)) {
            throw new JwtException("Invalid or retrieved token!");
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        if (isTokenRetrieved(token)) {
            return false;
        }

        try {
            Claims claims = jwtParser.parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Failed to parse token: {}", ex.getMessage());
            return false;
        }
    }

    private boolean isTokenRetrieved(String token) {
        Claims claims = jwtParser.parseToken(token);
        return tokenRepository.findTokenById(claims.getId())
                .isPresent();
    }
}
