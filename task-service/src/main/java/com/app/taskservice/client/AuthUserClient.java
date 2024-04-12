package com.app.taskservice.client;

import com.app.taskservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "localhost:8989")
public interface AuthUserClient {

    @GetMapping
    ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam int page,
                                                   @RequestParam int size);
}
