package com.app.gateway.config;

import com.app.gateway.validator.RouteValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public RouteValidator routeValidator() {
        return new RouteValidator();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

}
