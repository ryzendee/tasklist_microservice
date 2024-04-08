package com.app.authservice.service.authuser;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.exception.custom.InvalidCredentialsException;
import com.app.authservice.exception.custom.SignupException;
import com.app.authservice.exception.custom.UserNotFoundException;
import com.app.authservice.factory.authuser.AuthUserFactory;
import com.app.authservice.repository.authuser.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserFactory authUserFactory;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "auth_users", key = "#signupRequest.email")
    @Override
    public AuthUser createUser(SignUpRequest signupRequest) throws SignupException {
        AuthUser user = authUserFactory.createUserFromRequest(signupRequest, passwordEncoder);
        return saveUser(user);
    }

    private AuthUser saveUser(AuthUser user) {
        try {
            return authUserRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Exception while saving user: {}", ex.getMessage());
            throw new SignupException(ex.getMessage());
        }
    }

    @Cacheable(value = "auth_users", key = "#loginRequest.email")
    @Override
    public AuthUser loginUser(LoginRequest loginRequest) throws UserNotFoundException {
        AuthUser user = getAuthUserByEmail(loginRequest.email());

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            log.error("Failed to login user: {}", loginRequest.email());
            throw new InvalidCredentialsException("Incorrect password!");
        }

        return user;
    }

    private AuthUser getAuthUserByEmail(String email) {
        return authUserRepository.findAuthUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
