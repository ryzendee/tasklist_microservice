package com.app.authservice.mapper.impl;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.interfaces.AuthUserMapToTokenData;
import com.app.authservice.models.TokenData;
import org.springframework.stereotype.Component;

@Component
public class AuthUserToTokenDataMapper implements AuthUserMapToTokenData {

    @Override
    public TokenData map(AuthUser currentObject) {
        return new TokenData(
                String.valueOf(currentObject.getId()),
                currentObject.getRole().name()
        );
    }
}
