package com.app.authservice.unit.factory;


import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.Role;
import com.app.authservice.factory.authuser.AuthUserFactory;
import com.app.authservice.factory.authuser.AuthUserFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthUserFactoryTest {

    private AuthUserFactory authUserFactory;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        authUserFactory = new AuthUserFactoryImpl();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void createUserFromRequest_test_test() { 
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@gmail.com",
                "test-password",
                "test-password"
        );
        Role expectedRole = Role.ROLE_USER;

        AuthUser createdUser = authUserFactory.createUserFromRequest(signUpRequest, passwordEncoder);
        
        assertThat(createdUser.getEmail()).isEqualTo(signUpRequest.email());
        assertThat(createdUser.getRole()).isEqualTo(expectedRole);
        assertThat(passwordEncoder.matches(signUpRequest.password(), createdUser.getPassword())).isTrue();
    }

}
