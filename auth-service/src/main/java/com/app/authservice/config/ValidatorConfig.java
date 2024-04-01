package com.app.authservice.config;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    @Bean
    public EmailValidator emailValidator() {
        return EmailValidator.getInstance();
    }
}
