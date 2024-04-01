package com.app.authservice.service.sender;

import com.app.authservice.models.MailMessage;

public interface SenderService {
    void sendWelcomeMailMessage(String recieverEmail);
}
