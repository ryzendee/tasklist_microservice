package com.app.authservice.utils.jwt.parser;

import io.jsonwebtoken.Claims;

public interface JwtParser {
    Claims parseToken(String token);
}
