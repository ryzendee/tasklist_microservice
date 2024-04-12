package com.app.notificationservice.listeners;

import com.app.rabbit.mail.UserEmailDetails;
import com.app.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthUserEmailListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${email-exchange.queues.registration.name}")
    public void processEmailMessage(UserEmailDetails userEmailDetails) {
        log.info("Received message: {}", userEmailDetails);
        emailService.sendWelcomeMessage(userEmailDetails);
    }
}
