package com.app.taskservice.utils.sender;

import com.app.mail.TaskEmailDetails;

public interface MailQueueSender {

    void send(TaskEmailDetails taskEmailDetails);
}
