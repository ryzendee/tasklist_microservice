package com.app.authservice.utils.jwt;

import com.app.authservice.enums.ClaimsKey;
import com.app.authservice.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration-time}")
    private long accessExpirationTime;
    @Value("${jwt.refresh-time}")
    private long refreshExpirationTime;
    private Key key;


    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateRefreshToken(String subject, Role role) {
        return generateToken(subject, role, refreshExpirationTime);
    }

    @Override
    public String generateAccessToken(String subject, Role role) {
        return generateToken(subject, role, accessExpirationTime);
    }

    private String generateToken(String subject, Role role, long expirationTime) throws IllegalArgumentException {
        validateTokenData(subject, role, expirationTime);

        Claims claims = generateClaims(subject, role, expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    private void validateTokenData(String subject, Role role, long expirationTime) {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject must not be blank or null: " + subject);
        }

        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }

        if (expirationTime <= 0) {
            throw new IllegalArgumentException("Expiration time must be greater than zero: " + expirationTime);
        }
    }

    private Claims generateClaims(String subject, Role role, long expirationTime) {
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + expirationTime);
        String jti = generateJti();

        Claims claims = Jwts.claims();

        claims.setId(jti);
        claims.setSubject(subject);
        claims.setIssuedAt(issuedDate);
        claims.setExpiration(expirationDate);
        claims.put(ClaimsKey.ROLES.toString(), role);

        return claims;
    }

    private String generateJti() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getClaimsData(ClaimsKey key) {
        return null;
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException ex) {

            log.error("Invalid token exception: {}", ex.getMessage());
            return false;
        }
    }
}
