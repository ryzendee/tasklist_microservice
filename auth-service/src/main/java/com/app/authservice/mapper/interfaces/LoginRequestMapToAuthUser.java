package com.app.authservice.mapper.interfaces;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.MapToEntity;

public interface LoginRequestMapToAuthUser extends MapToEntity<AuthUser, LoginRequest> {
}
