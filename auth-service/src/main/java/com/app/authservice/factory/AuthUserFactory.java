package com.app.authservice.factory;

import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
public class AuthUserFactory {

    public static AuthUser createUser(SignUpRequest request, PasswordEncoder passwordEncoder) {
        AuthUser authUser = new AuthUser();
        authUser.setEmail(request.email());
        authUser.setRole(Role.ROLE_USER);
        authUser.setPassword(passwordEncoder.encode(request.password()));

        return authUser;
    }
}
