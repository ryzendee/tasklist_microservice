package com.app.authservice.facade;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.dto.response.AuthUserResponse;
import com.app.authservice.dto.response.TokenResponse;
import org.springframework.data.domain.Page;

public interface AuthFacade {

    TokenResponse signUpUser(SignUpRequest signUpRequest);
    TokenResponse loginUser(LoginRequest loginRequest);
    TokenResponse refreshAccessToken(String refreshToken);
    void retrieveRefreshToken(String refreshToken);
    boolean isTokenValid(String token);
    Page<AuthUserResponse> getUsersPage(int page, int pageSize);
}
