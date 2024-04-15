package com.app.taskservice.client;

import com.app.taskservice.dto.response.AuthUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthUserClient {

    @GetMapping("/api/v1/auth")
    Page<AuthUserResponse> getAllUsers(@RequestParam int page,
                                                       @RequestParam int size);
}
