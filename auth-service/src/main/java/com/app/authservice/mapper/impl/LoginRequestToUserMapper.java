package com.app.authservice.mapper.impl;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.MapToEntity;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestToUserMapper implements MapToEntity<AuthUser, LoginRequest> {
    @Override
    public AuthUser toEntity(LoginRequest dto) {
        return new AuthUser(dto.email(), dto.password());
    }
}
