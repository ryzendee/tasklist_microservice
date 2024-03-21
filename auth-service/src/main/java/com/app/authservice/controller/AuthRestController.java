package com.app.authservice.controller;

import com.app.authservice.dto.request.*;
import com.app.authservice.dto.response.*;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.service.authuser.AuthUserService;
import com.app.authservice.service.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final String AUTH_HEADER = "Authorization";

    private final AuthUserService authUserService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signUpUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        AuthUser createdUser = authUserService.createuser(signUpRequest);
        return getTokenResponse(createdUser);
    }

    @PostMapping("/login")
    public TokenResponse loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthUser user = authUserService.loginUser(loginRequest);
        return getTokenResponse(user);
    }

    private TokenResponse getTokenResponse(AuthUser user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public TokenResponse refreshAccessToken(@RequestHeader(AUTH_HEADER) String refreshToken) {
        String accessToken = jwtService.refreshAccessToken(refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    @PostMapping("/retrieveToken")
    public void retrieveRefreshToken(@RequestHeader(AUTH_HEADER) String refreshToken) {
        jwtService.retrieveRefreshToken(refreshToken);
    }
}
