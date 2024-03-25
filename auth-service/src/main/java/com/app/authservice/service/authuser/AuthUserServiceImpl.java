package com.app.authservice.service.authuser;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.exception.custom.InvalidPasswordException;
import com.app.authservice.exception.custom.SignupException;
import com.app.authservice.mapper.interfaces.SignUpRequestToAuthUserMapper;
import com.app.authservice.repository.AuthUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpRequestToAuthUserMapper signUpRequestToAuthUserMapper;

    @Override
    public AuthUser createUser(SignUpRequest signupRequest) throws SignupException {
        AuthUser user = signUpRequestToAuthUserMapper.map(signupRequest);
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
    @Override
    public AuthUser loginUser(LoginRequest loginRequest) throws EntityNotFoundException {
        AuthUser user = getAuthUserByEmail(loginRequest.email());

        if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException("Incorrect password!");
        }

        return user;
    }

    private AuthUser getAuthUserByEmail(String email) {
        return authUserRepository.findAuthUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
    }
}
