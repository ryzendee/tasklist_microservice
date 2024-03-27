package com.app.authservice.utils.jwt.generator;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.ClaimsKey;
import com.app.authservice.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtClaimsCreatorImpl implements JwtClaimsCreator {


    private final long refreshExpirationTime;
    private final long accessExpirationTime;

    @Override
    public Claims getTokenClaims(AuthUser authUser, TokenType tokenType) {
        long expirationTime = tokenType.equals(TokenType.REFRESH)
                ? refreshExpirationTime
                : accessExpirationTime;

        Claims claims = getBasicClaims(expirationTime);
        claims.setSubject(String.valueOf(authUser.getId()));
        claims.put(ClaimsKey.ROLES.name(), authUser.getRole().name());

        return claims;
    }

    private Claims getBasicClaims(long expirationTime) {
        Claims claims = Jwts.claims();
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + expirationTime);
        String jti = generateJti();

        claims.setId(jti);
        claims.setIssuedAt(issuedDate);
        claims.setExpiration(expirationDate);

        return claims;
    }

    @Override
    public Claims refreshClaimsForAccessToken(Claims oldClaims) {
        String role = oldClaims.get(ClaimsKey.ROLES.name(), String.class);

        Claims claims = getBasicClaims(accessExpirationTime);
        claims.setSubject(oldClaims.getSubject());
        claims.put(ClaimsKey.ROLES.name(), role);

        return claims;
    }

    private String generateJti() {
        return UUID.randomUUID().toString();
    }

}

