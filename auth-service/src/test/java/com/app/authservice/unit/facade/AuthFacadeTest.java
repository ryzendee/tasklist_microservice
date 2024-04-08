package com.app.authservice.unit.facade;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.dto.response.TokenResponse;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.Role;
import com.app.authservice.enums.ValidAuthData;
import com.app.authservice.facade.AuthFacadeImpl;
import com.app.authservice.service.authuser.AuthUserService;
import com.app.authservice.service.jwt.JwtService;
import com.app.authservice.service.sender.SenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthFacadeTest {

    private static final String VALID_EMAIL = ValidAuthData.EMAIL.getValue();
    private static final String VALID_PASSWORD = ValidAuthData.PASSWORD.getValue();
    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    @InjectMocks
    private AuthFacadeImpl authFacade;

    @Mock
    private AuthUserService authUserService;
    @Mock
    private JwtService jwtService;

    @Mock
    private SenderService senderService;

    @Test
    void signUpUser_withRequest_responsesShouldBeEquals() {
        SignUpRequest request = new SignUpRequest(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
        AuthUser user = getAuthUser();
        TokenResponse expectedResponse = getTokenResponseWithTokens();

        when(authUserService.createUser(request))
                .thenReturn(user);
        doNothing()
                .when(senderService).sendWelcomeMailMessage(user);
        when(jwtService.generateAccessToken(user))
                .thenReturn(ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(user))
                .thenReturn(REFRESH_TOKEN);

        TokenResponse actualResponse = authFacade.signUpUser(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(authUserService).createUser(request);
        verify(jwtService).generateAccessToken(user);
        verify(jwtService).generateRefreshToken(user);
    }

    @Test
    void loginUser_withRequest_responsesShouldBeEquals() {
        LoginRequest request = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        AuthUser user = getAuthUser();
        TokenResponse expectedResponse = getTokenResponseWithTokens();

        when(authUserService.loginUser(request))
                .thenReturn(user);
        when(jwtService.generateAccessToken(user))
                .thenReturn(ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(user))
                .thenReturn(REFRESH_TOKEN);

        TokenResponse actualResponse = authFacade.loginUser(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(authUserService).loginUser(request);
        verify(jwtService).generateAccessToken(user);
        verify(jwtService).generateRefreshToken(user);
    }

    private AuthUser getAuthUser() {
        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRole(Role.ROLE_USER);

        return user;
    }

    private TokenResponse getTokenResponseWithTokens() {
        return new TokenResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    @Test
    void refreshAccessToken_withToken_responsesShouldBeEquals() {
        TokenResponse expectedResponse = getTokenResponseWithTokens();

        when(jwtService.refreshAccessToken(REFRESH_TOKEN))
                .thenReturn(ACCESS_TOKEN);

        TokenResponse actualResponse = authFacade.refreshAccessToken(REFRESH_TOKEN);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(jwtService).refreshAccessToken(REFRESH_TOKEN);
    }

    @Test
    void retrieveRefreshToken_withToken_shouldInvokeJwtService() {
        doNothing().when(jwtService)
                .retrieveRefreshToken(REFRESH_TOKEN);

        authFacade.retrieveRefreshToken(REFRESH_TOKEN);

        verify(jwtService).retrieveRefreshToken(REFRESH_TOKEN);
    }

    @Test
    void isTokenValid_withToken_resultShouldBeTrue() {
        when(authFacade.isTokenValid(ACCESS_TOKEN))
                .thenReturn(true);

        boolean validationResult = authFacade.isTokenValid(ACCESS_TOKEN);

        assertThat(validationResult).isTrue();

        verify(jwtService).isTokenValid(ACCESS_TOKEN);
    }
}
