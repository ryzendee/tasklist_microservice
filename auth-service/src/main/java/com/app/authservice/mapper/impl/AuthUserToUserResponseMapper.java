package com.app.authservice.mapper.impl;

import com.app.authservice.dto.response.AuthUserResponse;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.AuthUserToUserResponseMap;
import org.springframework.stereotype.Component;

@Component
public class AuthUserToUserResponseMapper implements AuthUserToUserResponseMap {

    @Override
    public AuthUserResponse map(AuthUser authUser) {
        return new AuthUserResponse(authUser.getId(), authUser.getEmail());
    }
}
