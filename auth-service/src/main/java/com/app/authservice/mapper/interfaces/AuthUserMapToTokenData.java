package com.app.authservice.mapper.interfaces;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.MapToTarget;
import com.app.authservice.models.TokenData;

public interface AuthUserMapToTokenData extends MapToTarget<AuthUser, TokenData> {
}
