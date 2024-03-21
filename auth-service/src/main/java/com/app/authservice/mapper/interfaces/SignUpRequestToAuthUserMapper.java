package com.app.authservice.mapper.interfaces;

import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.MapToEntity;

public interface SignUpRequestToAuthUserMapper extends MapToEntity<AuthUser, SignUpRequest> {
}
