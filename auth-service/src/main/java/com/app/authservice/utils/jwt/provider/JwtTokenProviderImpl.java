package com.app.authservice.utils.jwt.provider;

import com.app.authservice.enums.ClaimsKey;
import com.app.authservice.models.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private static final String ROLE = ClaimsKey.ROLES.name();
    private final String secret;
    private final long accessExpirationTime;

    private final long refreshExpirationTime;
    private final Key key;


    public JwtTokenProviderImpl(@Value("${jwt.secret}") String secret,
                                @Value("${jwt.access-expiration}") long accessExpirationTime,
                                @Value("${jwt.refresh-expiration}") long refreshExpirationTime) {
        if (secret.isBlank()) {
            throw new IllegalArgumentException("Secret must not be blank");
        }

        if (accessExpirationTime <= 0 || refreshExpirationTime <= 0) {
            throw new IllegalArgumentException(String.format("Wrong time values. Actual values: accessExpiration=%d, refreshExpiration=%d",
                    accessExpirationTime, refreshExpirationTime));
        }

        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateRefreshToken(TokenData tokenData) {
        return generateToken(tokenData, refreshExpirationTime);
    }

    @Override
    public String generateAccessToken(TokenData tokenData) {
        return generateToken(tokenData, accessExpirationTime);
    }

    private String generateToken(TokenData tokenData, long expirationTime) throws IllegalArgumentException {
        validateTokenData(tokenData);

        Claims claims = generateClaims(tokenData, expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    private void validateTokenData(TokenData tokenData) {
        if (StringUtils.isBlank(tokenData.getSubject())) {
            throw new IllegalArgumentException("Subject must not be null or blank: " + tokenData.getSubject());
        }

        if (StringUtils.isBlank(tokenData.getRole())) {
            throw new IllegalArgumentException("Role must not be null or blank: " + tokenData.getRole());
        }
    }

    private Claims generateClaims(TokenData tokenData, long expirationTime) {
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + expirationTime);
        String jti = generateJti();

        Claims claims = Jwts.claims();
        claims.setId(jti);
        claims.setSubject(tokenData.getSubject());
        claims.setIssuedAt(issuedDate);
        claims.setExpiration(expirationDate);
        claims.put(ROLE, tokenData.getRole());

        return claims;
    }

    private String generateJti() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        try {
            Claims claims = parseToken(refreshToken);
            String subject = claims.getSubject();
            String role = claims.get(ROLE, String.class);

            return generateAccessToken(new TokenData(subject, role));
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Refresh access token exception : {}", ex.getMessage());
            throw new JwtException("Failed to refresh access token: " + ex.getMessage());
        }
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Failed to parse token: {}", ex.getMessage());
            throw ex;
        }
    }

}
