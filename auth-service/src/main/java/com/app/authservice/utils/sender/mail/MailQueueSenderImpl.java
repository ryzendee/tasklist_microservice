package com.app.authservice.utils.sender.mail;

import com.app.authservice.models.mail.UserEmailDetails;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailQueueSenderImpl implements MailQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public MailQueueSenderImpl(RabbitTemplate rabbitTemplate,
                               @Value("${email-exchange.name}") String exchange,
                               @Value("${email-exchange.queues[0].routing}")  String routingKey) {

        if (rabbitTemplate == null) {
            throw new IllegalArgumentException("Rabbit template must not be null");
        }

        if (StringUtils.isBlank(exchange)) {
            throw new IllegalArgumentException("Mail exchange must not be null or blank");
        }

        if (StringUtils.isBlank(routingKey)) {
            throw new IllegalArgumentException("Mail routing must not be null or blank");
        }

        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void sendMailToQueue(UserEmailDetails userEmailDetails) throws AmqpException {
        rabbitTemplate.convertAndSend(exchange, routingKey, userEmailDetails);
    }
}
