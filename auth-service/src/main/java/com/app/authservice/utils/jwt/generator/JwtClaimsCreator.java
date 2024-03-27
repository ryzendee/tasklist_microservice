package com.app.authservice.utils.jwt.generator;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.TokenType;
import io.jsonwebtoken.Claims;

public interface JwtClaimsCreator {

    Claims getTokenClaims(AuthUser authUser, TokenType tokenType);
    Claims refreshClaimsForAccessToken(Claims claims);
}
