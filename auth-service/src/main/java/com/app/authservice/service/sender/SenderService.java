package com.app.authservice.service.sender;

import com.app.authservice.entity.AuthUser;

public interface SenderService {
    void sendWelcomeMailMessage(AuthUser authUser);
}
