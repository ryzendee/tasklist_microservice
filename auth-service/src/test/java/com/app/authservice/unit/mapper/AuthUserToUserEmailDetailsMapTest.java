package com.app.authservice.unit.mapper;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.mapper.AuthUserToUserEmailDetailsMap;
import com.app.authservice.mapper.impl.AuthUserToUserEmailDetailsMapper;
import com.app.authservice.models.mail.UserEmailDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthUserToUserEmailDetailsMapTest {

    private AuthUserToUserEmailDetailsMap authUserToUserEmailDetailsMap;

    @BeforeEach
    void setUp() {
        authUserToUserEmailDetailsMap = new AuthUserToUserEmailDetailsMapper();
    }

    @Test
    void map_withEmail_emailsShouldBeEquals() {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("test@gmail.com");

        UserEmailDetails userEmailDetails = authUserToUserEmailDetailsMap.map(authUser);

        assertThat(userEmailDetails.recipientEmail()).isEqualTo(authUser.getEmail());
    }
}
