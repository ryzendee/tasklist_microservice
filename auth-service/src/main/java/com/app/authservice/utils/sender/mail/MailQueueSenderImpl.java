package com.app.authservice.utils.sender.mail;

import com.app.authservice.models.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailQueueSenderImpl implements MailQueueSender {

    private final RabbitTemplate rabbitTemplate;

    private String exchange;
    private String routingKey;

    @Override
    public void sendMailToQueue(MailMessage mailMessage) {
        rabbitTemplate.convertAndSend(exchange, routingKey, mailMessage);
    }
}
