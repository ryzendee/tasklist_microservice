package com.app.notificationservice.config;

import com.app.notificationservice.listeners.AuthUserEmailListener;
import com.app.notificationservice.listeners.TaskEmailListener;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MessageListener userEmailMessageListener(AuthUserEmailListener authUserEmailListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter();
        adapter.setDefaultListenerMethod("processEmailMessage");
        adapter.setMessageConverter(jackson2JsonMessageConverter());
        adapter.setDelegate(authUserEmailListener);

        return adapter;
    }

    @Bean
    public MessageListener taskReportEmailMessageListener(TaskEmailListener taskEmailListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter();
        adapter.setDelegate(taskEmailListener);
        adapter.setDefaultListenerMethod("processEmailMessage");
        adapter.setMessageConverter(jackson2JsonMessageConverter());

        return adapter;
    }

}
