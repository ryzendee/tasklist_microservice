package com.app.authservice.entity;

import com.app.authservice.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class AuthUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email must not be null")
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "User role is required")
    private Role role;

    public AuthUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
