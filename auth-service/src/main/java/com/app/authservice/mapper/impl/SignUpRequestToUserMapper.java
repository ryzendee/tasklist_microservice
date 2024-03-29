package com.app.authservice.mapper.impl;

import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.interfaces.SignUpRequestToAuthUserMapper;
import org.springframework.stereotype.Component;

@Component
public class SignUpRequestToUserMapper implements SignUpRequestToAuthUserMapper {

    @Override
    public AuthUser map(SignUpRequest dto) {
        return new AuthUser(dto.email(), dto.password());
    }
}
