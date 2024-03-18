package com.app.userservice.mapper.user;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.entity.user.UserEntity;
import com.app.userservice.mapper.MapToEntity;

public interface CreateRequestUserMapper extends MapToEntity<UserEntity, CreateUserRequest> {
}
