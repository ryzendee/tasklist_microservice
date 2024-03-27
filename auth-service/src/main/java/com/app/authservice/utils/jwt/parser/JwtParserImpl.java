package com.app.authservice.utils.jwt.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.security.Key;

@RequiredArgsConstructor
public class JwtParserImpl implements JwtParser {

    private final Key key;
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
