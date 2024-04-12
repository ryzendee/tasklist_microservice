package com.app.taskservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${email-exchange.name}")
    private String emailExchangeName;

    @Value("${email-exchange.queues.task-report.name}")
    private String taskReportEmailQueueName;
    @Value("${email-exchange.queues.task-report.routing}")
    private String taskReportEmailRoutingKey;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(emailExchangeName);
    }

    @Bean
    public Queue taskReportEmailQueue() {
        return new Queue(taskReportEmailQueueName);
    }

    @Bean
    public Binding bindingTaskReportEmailQueue(Queue taskReportEmailQueue, DirectExchange emailExchangeName) {
        return BindingBuilder.bind(taskReportEmailQueue).to(emailExchangeName).with(taskReportEmailRoutingKey);
    }
}
