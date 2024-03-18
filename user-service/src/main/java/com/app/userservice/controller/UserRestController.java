package com.app.userservice.controller;

import com.app.userservice.dto.request.CreateUserRequest;
import com.app.userservice.dto.response.UserResponse;
import com.app.userservice.entity.user.UserEntity;
import com.app.userservice.mapper.user.UserEntityResponseMapper;
import com.app.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final UserEntityResponseMapper userEntityResponseMapper;

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        UserEntity createdUser = userService.createUser(request);
        return userEntityResponseMapper.toDto(createdUser);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userEntityResponseMapper::toDto)
                .toList();

    }

}
