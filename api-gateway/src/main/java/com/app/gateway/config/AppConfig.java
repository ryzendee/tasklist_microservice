package com.app.gateway.config;

import com.app.gateway.validator.RouteValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RouteValidator routeValidator() {
        return new RouteValidator();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
