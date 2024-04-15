package com.app.taskservice.service.impl;

import com.app.taskservice.client.AuthUserClient;
import com.app.taskservice.dto.response.AuthUserResponse;
import com.app.taskservice.service.AuthUserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserClient authUserClient;

    @Override
    public Page<AuthUserResponse> sendRequestToGetUsers(int page, int size) throws FeignException {
        return authUserClient.getAllUsers(page, size);
    }
}
