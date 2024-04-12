package com.app.taskservice.service.impl;

import com.app.taskservice.client.AuthUserClient;
import com.app.taskservice.dto.response.UserResponse;
import com.app.taskservice.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserClient authUserClient;

    @Override
    public Page<UserResponse> sendRequestToGetUsers(int page, int size) {
        ResponseEntity<Page<UserResponse>> usersPage = authUserClient.getAllUsers(page, size);

        if (!usersPage.getStatusCode().is2xxSuccessful()) {
            throw new HttpClientErrorException(usersPage.getStatusCode());
        }

        return usersPage.getBody();
    }
}
