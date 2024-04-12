package com.app.authservice.utils.sender.mail.impl;

import com.app.authservice.utils.sender.mail.MailQueueSender;
import com.app.rabbit.mail.UserEmailDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserGreetingQueueSender implements MailQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public UserGreetingQueueSender(@NotNull  RabbitTemplate rabbitTemplate,
                                   @Value("${email-exchange.name}") @NotBlank String exchange,
                                   @Value("${email-exchange.queues.registration.routing}")  @NotBlank String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void sendMailToQueue(UserEmailDetails userEmailDetails) throws AmqpException {
        rabbitTemplate.convertAndSend(exchange, routingKey, userEmailDetails);
    }
}
