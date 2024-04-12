package com.app.taskservice.service;

import com.app.taskservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface AuthUserService {

    Page<UserResponse> sendRequestToGetUsers(int page, int size);
}
