package com.app.notificationservice.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailMessage {
    private final String recipientEmail;
    private String subject;
    private String text;
}
