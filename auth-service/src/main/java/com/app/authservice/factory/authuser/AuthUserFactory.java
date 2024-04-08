package com.app.authservice.factory.authuser;

import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface AuthUserFactory {

    AuthUser createUserFromRequest(SignUpRequest request, PasswordEncoder passwordEncoder);
}
