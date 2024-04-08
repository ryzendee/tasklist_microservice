package com.app.authservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidAuthData {
    EMAIL("test@gmail.com"),
    PASSWORD("testpassword");

    private final String value;
}
