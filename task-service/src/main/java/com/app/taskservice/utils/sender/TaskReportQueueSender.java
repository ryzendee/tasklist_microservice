package com.app.taskservice.utils.sender;

import com.app.mail.TaskEmailDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskReportQueueSender implements MailQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public TaskReportQueueSender(@NotNull RabbitTemplate rabbitTemplate,
                                 @Value("${email-exchange.name}") @NotBlank String exchange,
                                 @Value("${email-exchange.queues.registration.routing}")  @NotBlank String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }


    @Override
    public void send(TaskEmailDetails taskEmailDetails) {
        rabbitTemplate.convertAndSend(exchange, routingKey, taskEmailDetails);
    }
}
