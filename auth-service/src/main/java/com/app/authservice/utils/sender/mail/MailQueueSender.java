package com.app.authservice.utils.sender.mail;

import com.app.rabbit.mail.UserEmailDetails;

public interface MailQueueSender {

    void sendMailToQueue(UserEmailDetails userEmailDetails);
}
