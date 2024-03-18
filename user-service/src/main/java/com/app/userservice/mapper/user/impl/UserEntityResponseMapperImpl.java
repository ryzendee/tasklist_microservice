package com.app.userservice.mapper.user.impl;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.dto.response.UserResponse;
import com.app.userservice.entity.user.UserEntity;
import com.app.userservice.mapper.user.UserEntityResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEntityResponseMapperImpl implements UserEntityResponseMapper {

    @Override
    public UserResponse toDto(UserEntity entity) {
        return null;
    }
}
