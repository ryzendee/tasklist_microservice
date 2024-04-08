package com.app.authservice.controller;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.dto.response.TokenResponse;
import com.app.authservice.facade.AuthFacade;
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

    private final AuthFacade authFacade;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signUpUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authFacade.signUpUser(signUpRequest);
    }

    @PostMapping("/login")
    public TokenResponse loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authFacade.loginUser(loginRequest);
    }

    @PostMapping("/refresh")
    public TokenResponse refreshAccessToken(@RequestHeader(AUTH_HEADER) String refreshToken) {
        return authFacade.refreshAccessToken(refreshToken);
    }

    @PostMapping("/retrieve")
    public ResponseEntity<Void> retrieveRefreshToken(@RequestHeader(AUTH_HEADER) String refreshToken) {
        return authFacade.retrieveRefreshToken(refreshToken)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader(AUTH_HEADER) String token) {
        return authFacade.isTokenValid(token)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
