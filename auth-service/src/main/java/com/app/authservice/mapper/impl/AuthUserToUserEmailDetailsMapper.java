package com.app.authservice.mapper.impl;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.AuthUserToUserEmailDetailsMap;
import com.app.authservice.models.mail.UserEmailDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUserToUserEmailDetailsMapper implements AuthUserToUserEmailDetailsMap {

    @Override
    public UserEmailDetails map(AuthUser authUser) {
        return new UserEmailDetails(authUser.getEmail());
    }
}
