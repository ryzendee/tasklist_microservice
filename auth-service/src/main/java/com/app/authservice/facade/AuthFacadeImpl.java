package com.app.authservice.facade;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.dto.response.TokenResponse;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.exception.custom.GenerateTokenException;
import com.app.authservice.exception.custom.InvalidCredentialsException;
import com.app.authservice.exception.custom.SignupException;
import com.app.authservice.service.authuser.AuthUserService;
import com.app.authservice.service.jwt.JwtService;
import com.app.authservice.service.sender.SenderService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {


    private final AuthUserService authUserService;
    private final SenderService senderService;
    private final JwtService jwtService;

    @Override
    public TokenResponse signUpUser(SignUpRequest signUpRequest) throws SignupException, GenerateTokenException {
        AuthUser createdUser = authUserService.createUser(signUpRequest);
        senderService.sendWelcomeMailMessage(createdUser);

        return createTokenResponseForUser(createdUser);
    }

    @Override
    public TokenResponse loginUser(LoginRequest loginRequest) throws InvalidCredentialsException, GenerateTokenException {
        AuthUser user = authUserService.loginUser(loginRequest);

        return createTokenResponseForUser(user);
    }

    @Override
    public TokenResponse refreshAccessToken(String refreshToken) throws JwtException {
        String accessToken = jwtService.refreshAccessToken(refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    private TokenResponse createTokenResponseForUser(AuthUser user) throws GenerateTokenException {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    public void retrieveRefreshToken(String refreshToken) throws JwtException {
        jwtService.retrieveRefreshToken(refreshToken);
    }

    @Override
    public boolean isTokenValid(String token) {
        return jwtService.isTokenValid(token);
    }
}
