package com.app.authservice.models;

import lombok.Data;

@Data
public class TokenData {

    private final String subject;
    private final String role;
}
