package com.app.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        String name,
        @Email(message = "Incorrect email format")
        @NotNull(message = "Email must not be null")
        String email,
        @NotBlank(message = "Password must not be blank")
        @NotNull(message = "Password must not be null")
        String password

) {
}
