package com.app.authservice.unit.repository;

import com.app.authservice.repository.token.TokenRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenRepositoryTest {

    private static final String JTI = "test-jti";
    private static final String TOKEN = "test-token";
    @InjectMocks
    private TokenRepositoryImpl tokenRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);
    }

    @AfterEach
    void checkInvocation() {
        verify(redisTemplate).opsForValue();
    }

    @Test
    void findTokenById_existsToken_returnsOptionalWithToken() {
        when(valueOperations.get(JTI))
                .thenReturn(TOKEN);

        Optional<String> actualOptional = tokenRepository.findTokenById(JTI);

        assertThat(actualOptional)
                .isNotEmpty()
                .contains(TOKEN);
        verify(valueOperations).get(JTI);
    }

    @Test
    void findTokenById_nonExistsToken_returnsEmptyOptional() {
        when(valueOperations.get(JTI))
                .thenReturn(null);

        Optional<String> actualOptional = tokenRepository.findTokenById(JTI);

        assertThat(actualOptional).isEmpty();
        verify(valueOperations).get(JTI);
    }

    @Test
    void saveTokenWithExpiration_validToken_savesToken() {
        long expirationTime = 10;

        doNothing()
                .when(valueOperations).set(JTI, TOKEN, expirationTime, TimeUnit.MILLISECONDS);

        String actualJti = tokenRepository.saveTokenWithExpiration(JTI, TOKEN, expirationTime);

        assertThat(actualJti).isEqualTo(JTI);
        verify(valueOperations).set(JTI, TOKEN, expirationTime, TimeUnit.MILLISECONDS);
    }
}
