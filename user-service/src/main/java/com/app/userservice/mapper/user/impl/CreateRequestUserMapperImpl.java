package com.app.userservice.mapper.user.impl;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.entity.user.UserEntity;
import com.app.userservice.mapper.user.CreateRequestUserMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateRequestUserMapperImpl implements CreateRequestUserMapper {

    @Override
    public UserEntity toEntity(CreateUserRequest request) {
        return null;
    }
}
