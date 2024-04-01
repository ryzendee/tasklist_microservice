package com.app.authservice.exception.custom;

public class MailMessageException extends RuntimeException {
    public MailMessageException(String message) {
        super(message);
    }
}
