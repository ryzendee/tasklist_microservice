package com.app.authservice.unit.utils.jwt;

import com.app.authservice.utils.jwt.parser.JwtParser;
import com.app.authservice.utils.jwt.parser.JwtParserImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtParserTest {

    private JwtParser jwtParser;
    private Key key;

    @BeforeEach
    void setUp() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtParser = new JwtParserImpl(key);
    }

    //TODO
    @Test
    void parseToken_validToken_returnsClaims() {
        Claims expectedClaims = generateClaims();
        String token = Jwts.builder()
                .setClaims(expectedClaims)
                .signWith(key)
                .compact();

        Claims actualClaims = jwtParser.parseToken(token);

        assertThat(actualClaims).isEqualTo(expectedClaims);
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        Claims expectedClaims = generateClaims();
        String token = Jwts.builder()
                .setClaims(expectedClaims)
                .signWith(key)
                .compact();

        boolean validity = jwtParser.isTokenValid(token);

        assertThat(validity).isTrue();
    }

    private Claims generateClaims() {
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + 1000000);
        Claims claims = Jwts.claims();
        claims.setSubject("test-sub");
        claims.setId("test-id");
        claims.setIssuedAt(issuedDate);
        claims.setExpiration(expirationDate);

        return claims;
    }

    @Test
    void isTokenValid_tokenIsExpired_returnsFalse() {
        Claims claims = generateClaims();
        claims.setExpiration(claims.getIssuedAt());

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();

        boolean validity = jwtParser.isTokenValid(expiredToken);

        assertThat(validity).isFalse();
    }

    @Test
    void isTokenValid_invalidSign_returnsFalse() {
        Key anotherKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);
        Claims claims = generateClaims();

        String signedToken = Jwts.builder()
                .setClaims(claims)
                .signWith(anotherKey)
                .compact();

        boolean validity = jwtParser.isTokenValid(signedToken);

        assertThat(validity).isFalse();
    }
    
}
