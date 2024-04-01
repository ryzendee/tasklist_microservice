package com.app.authservice.controller;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.dto.response.TokenResponse;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.service.authuser.AuthUserService;
import com.app.authservice.service.jwt.JwtService;
import com.app.authservice.service.sender.SenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;

    private final AuthUserService authUserService;
    private final SenderService senderService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signUpUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        AuthUser createdUser = authUserService.createUser(signUpRequest);
        senderService.sendWelcomeMailMessage(createdUser.getEmail());
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

    @PostMapping("/retrieve")
    public void retrieveRefreshToken(@RequestHeader(AUTH_HEADER) String refreshToken) {
        jwtService.retrieveRefreshToken(refreshToken);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader(AUTH_HEADER) String token) {
        return jwtService.isTokenValid(token)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
