package com.app.authservice.factory.authuser;

import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthUserFactoryImpl implements AuthUserFactory {


    @Override
    public AuthUser createUserFromRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        AuthUser authUser = new AuthUser();
        authUser.setEmail(request.email());
        authUser.setRole(Role.ROLE_USER);
        authUser.setPassword(passwordEncoder.encode(request.password()));

        return authUser;
    }
}
