package com.app.taskservice.service;

import com.app.taskservice.dto.response.AuthUserResponse;
import org.springframework.data.domain.Page;

public interface AuthUserService {

    Page<AuthUserResponse> sendRequestToGetUsers(int page, int size);
}
