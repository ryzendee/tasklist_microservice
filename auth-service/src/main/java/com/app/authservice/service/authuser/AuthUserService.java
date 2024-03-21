package com.app.authservice.service.authuser;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;

public interface AuthUserService {

    AuthUser createUser(SignUpRequest signupRequest);
    AuthUser loginUser(LoginRequest loginRequest);
}
