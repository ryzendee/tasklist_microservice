package com.app.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "Email must not be null")
        @Email(message = "Incorrect email-format")
        String email,

        @NotNull(message = "Password must not be null")
        @NotBlank(message = "Password must not be blank")
        String password
) {
}
