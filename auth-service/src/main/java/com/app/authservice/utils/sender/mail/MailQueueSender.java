package com.app.authservice.utils.sender.mail;

import com.app.authservice.models.MailMessage;

public interface MailQueueSender {

    void sendMailToQueue(MailMessage mailMessage);
}
