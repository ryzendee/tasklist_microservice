package com.app.authservice.utils.sender.mail;

import com.app.authservice.models.mail.UserEmailDetails;

public interface MailQueueSender {

    void sendMailToQueue(UserEmailDetails userEmailDetails);
}
