package com.app.authservice.dto.request;

import com.app.authservice.annotations.validation.password.PasswordMatcher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@PasswordMatcher
public record SignUpRequest(
        @Email(message = "Incorrect email format")
        @NotNull(message = "Email must not be null")
        @NotBlank(message = "Email must not be blank")
        String email,
        @NotNull(message = "Password must not be null")
        @NotBlank(message = "Password must not be blank")
        String password,
        @NotNull(message = "Password confirmation must not be null")
        @NotBlank(message = "Password confirmation must not be blank")
        String passwordConfirmation
) {
}
