package com.app.authservice.utils.jwt.factory;

import io.jsonwebtoken.Claims;

public interface JwtFactory {

    String createToken(Claims claims);
}
