package com.app.authservice.exception.custom;

public class SignupException extends RuntimeException {
    public SignupException(String message) {
        super(message);
    }
}
