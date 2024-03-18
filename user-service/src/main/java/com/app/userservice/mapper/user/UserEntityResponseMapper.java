package com.app.userservice.mapper.user;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.dto.response.UserResponse;
import com.app.userservice.entity.user.UserEntity;
import com.app.userservice.mapper.MapToDto;

public interface UserEntityResponseMapper extends MapToDto<UserEntity, UserResponse> {
}
