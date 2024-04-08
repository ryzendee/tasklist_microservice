package com.app.authservice.unit.utils.jwt;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.ClaimsKey;
import com.app.authservice.enums.Role;
import com.app.authservice.enums.TokenType;
import com.app.authservice.utils.jwt.generator.JwtClaimsCreator;
import com.app.authservice.utils.jwt.generator.JwtClaimsCreatorImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtClaimsGeneratorTest {
    private static final long ACCESS_EXP_TIME = 10000;
    private static final long REFRESH_EXP_TIME = 20000;
    private static final String ROLES_KEY = ClaimsKey.ROLES.name();
    private JwtClaimsCreator jwtClaimsCreator;

    @BeforeEach
    void setUp() {
        jwtClaimsCreator = new JwtClaimsCreatorImpl(ACCESS_EXP_TIME, REFRESH_EXP_TIME);
    }

    @Test
    void getTokenClaims_accessToken_ok() {
        AuthUser userForClaims = getUserForClaims();
        Claims claims = jwtClaimsCreator.getTokenClaims(userForClaims, TokenType.ACCESS);

        assertThat(claims.getSubject()).isEqualTo(String.valueOf(userForClaims.getId()));
        assertThat(claims.getId()).isNotBlank();
        assertThat(claims.getIssuedAt()).isBefore(new Date());
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
        assertThat(claims.get(ROLES_KEY, String.class)).isEqualTo(userForClaims.getRole().name());
    }

    @Test
    void getTokenClaims_refreshToken_ok() {
        AuthUser userForClaims = getUserForClaims();
        Claims claims = jwtClaimsCreator.getTokenClaims(userForClaims, TokenType.REFRESH);

        assertThat(claims.getSubject()).isEqualTo(String.valueOf(userForClaims.getId()));
        assertThat(claims.getId()).isNotBlank();
        assertThat(claims.getIssuedAt()).isBefore(new Date());
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
        assertThat(claims.get(ROLES_KEY, String.class)).isEqualTo(userForClaims.getRole().name());
    }

    private AuthUser getUserForClaims() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setRole(Role.ROLE_USER);

        return authUser;
    }

    @Test
    void refreshClaimsForAccessToken() {
        Claims claimsToUpdate = getClaims();
        Date issuedAt = new Date(claimsToUpdate.getExpiration().getTime() - 100_000);
        claimsToUpdate.setIssuedAt(issuedAt);

        Claims updatedClaims = jwtClaimsCreator.refreshClaimsForAccessToken(claimsToUpdate);

        assertThat(updatedClaims.getId()).isNotBlank();
        assertThat(updatedClaims.getSubject()).isEqualTo(claimsToUpdate.getSubject());
        assertThat(updatedClaims.getIssuedAt()).isAfter(claimsToUpdate.getIssuedAt());
        assertThat(updatedClaims.getExpiration()).isAfter(claimsToUpdate.getExpiration());
    }


    private Claims getClaims() {
        Claims claims = Jwts.claims();
        claims.setId("test-id");
        claims.setSubject("test-subject");
        claims.put(ClaimsKey.ROLES.name(), Role.ROLE_USER.name());

        Date issuedDate = new Date();
        claims.setIssuedAt(issuedDate);
        claims.setExpiration(new Date(issuedDate.getTime() + ACCESS_EXP_TIME));

        return claims;
    }

}
