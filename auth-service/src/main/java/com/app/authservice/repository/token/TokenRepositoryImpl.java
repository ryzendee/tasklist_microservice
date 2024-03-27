package com.app.authservice.repository.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<String> findTokenById(String jti) {
        String token = (String) redisTemplate.opsForValue().get(jti);
        return Optional.ofNullable(token);
    }

    @Override
    public String saveTokenWithExpiration(String jti, String token, long expirationTime) {
        redisTemplate.opsForValue().set(jti, token, expirationTime, TimeUnit.MILLISECONDS);
        return token;
    }
}
