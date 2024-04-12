package com.app.authservice.mapper;

import com.app.authservice.entity.AuthUser;
import com.app.rabbit.mail.UserEmailDetails;

public interface AuthUserToUserEmailDetailsMap {

    UserEmailDetails map(AuthUser authUser);
}
