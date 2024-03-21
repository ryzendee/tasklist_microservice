package com.app.authservice.mapper.impl;

import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.MapToEntity;
import org.springframework.stereotype.Component;

@Component
public class SignUpRequestToUserMapper implements MapToEntity<AuthUser, SignUpRequest> {

    @Override
    public AuthUser toEntity(SignUpRequest dto) {
        return new AuthUser(dto.email(), dto.password());
    }
}
