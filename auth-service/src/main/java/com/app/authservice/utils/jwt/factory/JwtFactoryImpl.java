package com.app.authservice.utils.jwt.factory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.security.Key;

@RequiredArgsConstructor
public class JwtFactoryImpl implements JwtFactory {

    private final Key key;

    @Override
    public String createToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }
}
