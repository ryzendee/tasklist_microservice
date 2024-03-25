package com.app.authservice.repository;

import com.app.authservice.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findAuthUserByEmail(String email);
}
