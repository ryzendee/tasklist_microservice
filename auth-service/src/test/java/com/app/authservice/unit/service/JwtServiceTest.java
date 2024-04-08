package com.app.authservice.unit.service;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.Role;
import com.app.authservice.enums.TokenType;
import com.app.authservice.exception.custom.GenerateTokenException;
import com.app.authservice.repository.token.TokenRepositoryImpl;
import com.app.authservice.service.jwt.JwtServiceImpl;
import com.app.authservice.utils.jwt.factory.JwtFactoryImpl;
import com.app.authservice.utils.jwt.generator.JwtClaimsCreatorImpl;
import com.app.authservice.utils.jwt.parser.JwtParserImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private static final String TOKEN = "test-token";
    private static final String JTI = "test-jti";
    @InjectMocks
    private JwtServiceImpl jwtService;
    @Mock
    private TokenRepositoryImpl tokenRepository;
    @Mock
    private JwtFactoryImpl jwtFactory;
    @Mock
    private JwtParserImpl jwtParser;
    @Mock
    private JwtClaimsCreatorImpl jwtClaimsCreator;
    @Mock
    private Claims mockClaims;

    @Test
    void generateAccessToken_validUser_returnsToken() {
        AuthUser validUser = getValidUser();

        when(jwtClaimsCreator.getTokenClaims(validUser, TokenType.ACCESS))
                .thenReturn(mockClaims);
        when(jwtFactory.createToken(mockClaims))
                .thenReturn(TOKEN);

        String actualToken = jwtService.generateAccessToken(validUser);
        assertThat(actualToken).isEqualTo(TOKEN);

        verify(jwtClaimsCreator).getTokenClaims(validUser, TokenType.ACCESS);
        verify(jwtFactory).createToken(mockClaims);
    }

    @Test
    void generateRefreshToken_validUser_returnsToken() {
        AuthUser validUser = getValidUser();

        when(jwtClaimsCreator.getTokenClaims(validUser, TokenType.REFRESH))
                .thenReturn(mockClaims);
        when(jwtFactory.createToken(mockClaims))
                .thenReturn(TOKEN);

        String actualToken = jwtService.generateRefreshToken(validUser);
        assertThat(actualToken).isEqualTo(TOKEN);

        verify(jwtClaimsCreator).getTokenClaims(validUser, TokenType.REFRESH);
        verify(jwtFactory).createToken(mockClaims);
    }

    private AuthUser getValidUser() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setEmail("test@gmail.com");
        authUser.setPassword("test-password");
        authUser.setRole(Role.ROLE_USER);
        return authUser;
    }

    @MethodSource("getInvalidUsers")
    @ParameterizedTest
    void generateAccessToken_invalidUser_throwsGenerationTokenEx(AuthUser invalidUser) {
        assertThatThrownBy(() -> jwtService.generateAccessToken(invalidUser))
                .isInstanceOf(GenerateTokenException.class);
    }

    @MethodSource("getInvalidUsers")
    @ParameterizedTest
    void generateRefreshToken_invalidUser_throwsGenerationTokenEx(AuthUser invalidUser) {
        assertThatThrownBy(() -> jwtService.generateRefreshToken(invalidUser))
                .isInstanceOf(GenerateTokenException.class);
    }

    static Stream<Arguments> getInvalidUsers() {
        AuthUser withNullId = new AuthUser();

        AuthUser withNullRole = new AuthUser();
        withNullRole.setId(1L);

        return Stream.of(
                Arguments.of(withNullId),
                Arguments.of(withNullRole)
        );
    }


    @Test
    void refreshAccessToken_tokenIsNull_throwsJwtException() {
        assertThatThrownBy(() -> jwtService.refreshAccessToken(null))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void refreshAccessToken_invalidToken_throwsJwtException() {
        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(false);

        assertThatThrownBy(() -> jwtService.refreshAccessToken(TOKEN))
                .isInstanceOf(JwtException.class);

        verify(jwtParser).isTokenValid(TOKEN);
    }

    @Test
    void refreshAccessToken_tokenIsRetrieved_throwsJwtException() {
        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(true);
        when(jwtParser.parseToken(TOKEN))
                .thenReturn(mockClaims);
        when(mockClaims.getId())
                .thenReturn(JTI);
        when(tokenRepository.findTokenById(JTI))
                .thenReturn(Optional.of(TOKEN));

        assertThatThrownBy(() -> jwtService.refreshAccessToken(TOKEN))
                .isInstanceOf(JwtException.class);

        verify(jwtParser).isTokenValid(TOKEN);
        verify(jwtParser).parseToken(TOKEN);
        verify(mockClaims).getId();
        verify(tokenRepository).findTokenById(JTI);
    }

    @Test
    void refreshAccessToken_validToken_returnsToken() {
        //One invocation in isTokenRetrieved and another in refreshClaimsForAccessToken
        int parseClaimsInvocationTimes = 2;

        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(true);
        when(tokenRepository.findTokenById(JTI))
                .thenReturn(Optional.empty());
        when(jwtParser.parseToken(TOKEN))
                .thenReturn(mockClaims);
        when(mockClaims.getId())
                .thenReturn(JTI);
        when(jwtClaimsCreator.refreshClaimsForAccessToken(mockClaims))
                .thenReturn(mockClaims);
        when(jwtFactory.createToken(mockClaims))
                .thenReturn(TOKEN);

        String actualToken = jwtService.refreshAccessToken(TOKEN);
        assertThat(actualToken).isEqualTo(TOKEN);

        verify(jwtParser).isTokenValid(TOKEN);
        verify(tokenRepository).findTokenById(JTI);
        verify(jwtParser, times(parseClaimsInvocationTimes)).parseToken(TOKEN);
        verify(mockClaims).getId();
        verify(jwtClaimsCreator).refreshClaimsForAccessToken(mockClaims);
        verify(jwtFactory).createToken(mockClaims);
    }

    @Test
    void retrieveToken_nullToken_throwJwtEx() {
        assertThatThrownBy(() -> jwtService.retrieveRefreshToken(null))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void retrieveToken_invalidToken_throwJwtEx() {
        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(false);

        assertThatThrownBy(() -> jwtService.retrieveRefreshToken(TOKEN))
                .isInstanceOf(JwtException.class);

        verify(jwtParser).isTokenValid(TOKEN);
    }

    @Test
    void retrieveToken_retrievedToken_throwJwtEx() {
        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(true);
        when(jwtParser.parseToken(TOKEN))
                .thenReturn(mockClaims);
        when(mockClaims.getId())
                .thenReturn(JTI);
        when(tokenRepository.findTokenById(JTI))
                .thenReturn(Optional.of(TOKEN));

        assertThatThrownBy(() -> jwtService.retrieveRefreshToken(TOKEN))
                .isInstanceOf(JwtException.class);

        verify(jwtParser).isTokenValid(TOKEN);
        verify(jwtParser).parseToken(TOKEN);
        verify(mockClaims).getId();
        verify(tokenRepository).findTokenById(JTI);
    }

    @Test
    void retrieveRefreshToken_validToken_retrievesToken() {
        //One invocation in isTokenRetrieved that in validateToken and another in retrieveRefreshToken
        int parseClaimsInvocationTimes = 2;

        //One invocation in isTokenRetrieved that in validateToken and another in saveTokenWithExpiration
        int getIdInvocationTimes = 2;

        Date expirationDate = new Date();

        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(true);
        when(tokenRepository.findTokenById(JTI))
                .thenReturn(Optional.empty());
        when(jwtParser.parseToken(TOKEN))
                .thenReturn(mockClaims);
        when(mockClaims.getId())
                .thenReturn(JTI);
        when(mockClaims.getExpiration())
                .thenReturn(expirationDate);
        when(tokenRepository.saveTokenWithExpiration(JTI, TOKEN, expirationDate.getTime()))
                .thenReturn(JTI);

        jwtService.retrieveRefreshToken(TOKEN);

        verify(jwtParser).isTokenValid(TOKEN);
        verify(tokenRepository).findTokenById(JTI);
        verify(jwtParser, times(parseClaimsInvocationTimes)).parseToken(TOKEN);
        verify(mockClaims, times(getIdInvocationTimes)).getId();
        verify(mockClaims).getExpiration();
        verify(tokenRepository).saveTokenWithExpiration(JTI, TOKEN, expirationDate.getTime());
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        when(jwtParser.isTokenValid(TOKEN))
                .thenReturn(true);
        when(jwtParser.parseToken(TOKEN))
                .thenReturn(mockClaims);
        when(mockClaims.getId())
                .thenReturn(JTI);
        when(tokenRepository.findTokenById(JTI))
                .thenReturn(Optional.empty());

        boolean tokenValidity = jwtService.isTokenValid(TOKEN);
        assertThat(tokenValidity).isTrue();

        verify(jwtParser).isTokenValid(TOKEN);
        verify(jwtParser).parseToken(TOKEN);
        verify(mockClaims).getId();
        verify(tokenRepository).findTokenById(JTI);
    }
}
