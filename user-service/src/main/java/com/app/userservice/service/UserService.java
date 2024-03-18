package com.app.userservice.service;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.entity.user.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity createUser(CreateUserRequest request);

    List<UserEntity> getAllUsers();

}
