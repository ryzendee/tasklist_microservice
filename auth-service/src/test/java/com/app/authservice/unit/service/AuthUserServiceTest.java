package com.app.authservice.unit.service;

import com.app.authservice.dto.request.LoginRequest;
import com.app.authservice.dto.request.SignUpRequest;
import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.Role;
import com.app.authservice.exception.custom.InvalidCredentialsException;
import com.app.authservice.exception.custom.SignupException;
import com.app.authservice.exception.custom.UserNotFoundException;
import com.app.authservice.factory.authuser.AuthUserFactoryImpl;
import com.app.authservice.repository.authuser.AuthUserRepository;
import com.app.authservice.service.authuser.AuthUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUserServiceTest {

    private static final String VALID_EMAIL = "test@gmail.com";
    private static final String VALID_PASSWORD = "testpassword";

    @InjectMocks
    private AuthUserServiceImpl authUserService;
    @Mock
    private AuthUserFactoryImpl authUserFactory;
    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createUser_validRequest_successSignUp() {
        SignUpRequest validRequest = getSignUpRequestWithValidData();
        AuthUser expectedUser = getUserWithValidData();

        when(authUserFactory.createUserFromRequest(validRequest, passwordEncoder))
                .thenReturn(expectedUser);
        when(authUserRepository.saveAndFlush(expectedUser))
                .thenReturn(expectedUser);

        AuthUser actualUser = authUserService.createUser(validRequest);
        assertThat(actualUser).isEqualTo(expectedUser);

        verify(authUserFactory).createUserFromRequest(validRequest, passwordEncoder);
        verify(authUserRepository).saveAndFlush(expectedUser);
    }

    @Test
    void createUser_nonUniqueEmail_throwsSignupEx() {
        String testExMessage = "test-ex-message";
        SignUpRequest requestWithNonUniqueEmail = getSignUpRequestWithValidData();
        AuthUser authUser = getUserWithValidData();

        when(authUserFactory.createUserFromRequest(requestWithNonUniqueEmail, passwordEncoder))
                .thenReturn(authUser);
        when(authUserRepository.saveAndFlush(authUser))
                .thenThrow(new DataIntegrityViolationException(testExMessage));

        assertThatThrownBy(() -> authUserService.createUser(requestWithNonUniqueEmail))
                .isInstanceOf(SignupException.class);

        verify(authUserFactory).createUserFromRequest(requestWithNonUniqueEmail, passwordEncoder);
        verify(authUserRepository).saveAndFlush(authUser);
    }

    @Test
    void loginUser_existsUser_successLogin() {
        LoginRequest loginRequest = getLoginRequestWithValidData();
        AuthUser expectedUser = getUserWithValidData();

        when(authUserRepository.findAuthUserByEmail(loginRequest.email()))
                .thenReturn(Optional.of(expectedUser));
        when(passwordEncoder.matches(loginRequest.password(), expectedUser.getPassword()))
                .thenReturn(true);

        AuthUser actualUser = authUserService.loginUser(loginRequest);
        assertThat(actualUser).isEqualTo(expectedUser);

        verify(authUserRepository).findAuthUserByEmail(loginRequest.email());
        verify(passwordEncoder).matches(loginRequest.password(), expectedUser.getPassword());
    }

    @Test
    void loginUser_nonExistsUser_throwsUserNotFoundException() {
        LoginRequest loginRequest = getLoginRequestWithValidData();

        when(authUserRepository.findAuthUserByEmail(loginRequest.email()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authUserService.loginUser(loginRequest))
                .isInstanceOf(UserNotFoundException.class);

        verify(authUserRepository).findAuthUserByEmail(loginRequest.email());
    }

    @Test
    void loginUser_invalidPassword_throwsInvalidCredentialsException() {
        LoginRequest loginRequest = getLoginRequestWithValidData();
        AuthUser authUser = getUserWithValidData();

        when(authUserRepository.findAuthUserByEmail(loginRequest.email()))
                .thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches(loginRequest.password(), authUser.getPassword()))
                .thenReturn(false);

        assertThatThrownBy(() -> authUserService.loginUser(loginRequest))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(authUserRepository).findAuthUserByEmail(loginRequest.email());
        verify(passwordEncoder).matches(loginRequest.password(), authUser.getPassword());
    }

    private SignUpRequest getSignUpRequestWithValidData() {
        return new SignUpRequest(VALID_EMAIL, VALID_PASSWORD, VALID_PASSWORD);
    }

    private LoginRequest getLoginRequestWithValidData() {
        return new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
    }

    private AuthUser getUserWithValidData() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setRole(Role.ROLE_USER);
        authUser.setEmail(VALID_EMAIL);
        authUser.setPassword(VALID_PASSWORD);

        return authUser;
    }
}
