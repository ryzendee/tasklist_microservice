package com.app.authservice.unit.utils.jwt;

import com.app.authservice.utils.jwt.factory.JwtFactory;
import com.app.authservice.utils.jwt.factory.JwtFactoryImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtFactoryTest {

    
    private JwtFactory jwtFactory;
    private Key key;

    @BeforeEach
    void setUp() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtFactory = new JwtFactoryImpl(key);
    }

    @Test
    void createToken_validClaims_returnsToken() {
        Claims claims = Jwts.claims();

        String actualToken = jwtFactory.createToken(claims);

        String expectedToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();

        assertThat(actualToken).isEqualTo(expectedToken);
    }
}
