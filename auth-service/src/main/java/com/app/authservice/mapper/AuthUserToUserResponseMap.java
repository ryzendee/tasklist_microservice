package com.app.authservice.mapper;

import com.app.authservice.dto.response.AuthUserResponse;
import com.app.authservice.entity.AuthUser;

public interface AuthUserToUserResponseMap {

    AuthUserResponse map(AuthUser authUser);
}
