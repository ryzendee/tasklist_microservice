package com.app.authservice.config;

import com.app.authservice.utils.jwt.factory.JwtFactory;
import com.app.authservice.utils.jwt.factory.JwtFactoryImpl;
import com.app.authservice.utils.jwt.generator.JwtClaimsCreator;
import com.app.authservice.utils.jwt.generator.JwtClaimsCreatorImpl;
import com.app.authservice.utils.jwt.parser.JwtParser;
import com.app.authservice.utils.jwt.parser.JwtParserImpl;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;

@Configuration
public class SecurityConfig {

    private final Key key;
    private final String secret;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;

    public SecurityConfig(@Value("${jwt.secret}") String secret,
                                @Value("${jwt.access-expiration}") long accessExpirationTime,
                                @Value("${jwt.refresh-expiration}") long refreshExpirationTime) {
        if (secret.isBlank()) {
            throw new IllegalArgumentException("Secret must not be blank");
        }

        if (accessExpirationTime <= 0 || refreshExpirationTime <= 0) {
            throw new IllegalArgumentException(String.format("Wrong time values. Actual values: accessExpiration=%d, refreshExpiration=%d",
                    accessExpirationTime, refreshExpirationTime));
        }

        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtParser jwtTokenParser() {
        return new JwtParserImpl(key);
    }

    @Bean
    public JwtClaimsCreator jwtTokenGenerator() {
        return new JwtClaimsCreatorImpl(accessExpirationTime, refreshExpirationTime);
    }

    @Bean
    public JwtFactory jwtFactory() {
        return new JwtFactoryImpl(key);
    }
}
