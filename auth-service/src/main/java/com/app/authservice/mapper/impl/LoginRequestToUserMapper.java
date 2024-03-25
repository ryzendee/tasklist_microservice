package com.app.authservice.mapper.impl;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.interfaces.LoginRequestMapToAuthUser;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestToUserMapper implements LoginRequestMapToAuthUser {
    @Override
    public AuthUser map(LoginRequest dto) {
        return new AuthUser(dto.email(), dto.password());
    }
}
