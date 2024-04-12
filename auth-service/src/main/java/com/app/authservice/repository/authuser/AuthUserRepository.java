package com.app.authservice.repository.authuser;

import com.app.authservice.entity.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findAuthUserByEmail(String email);

    Page<AuthUser> findAll(Pageable pageable);
}
