package com.app.authservice.repository.token;

import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TokenRepository {

    Optional<String> findTokenById(String jti);
    String saveTokenWithExpiration(String jti, String token, long expirationTime);
}
