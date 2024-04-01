package com.app.authservice.exception.custom;

public class MessageSendingException extends RuntimeException {
    public MessageSendingException(String message) {
        super(message);
    }
}
