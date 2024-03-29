package com.app.authservice.repository.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Optional<String> findTokenById(String jti) {
        String token = redisTemplate.opsForValue().get(jti);
        return Optional.ofNullable(token);
    }

    @Override
    public String saveTokenWithExpiration(String jti, String token, long expirationTime) {
        redisTemplate.opsForValue().set(jti, token, expirationTime, TimeUnit.MILLISECONDS);
        return jti;
    }
}
