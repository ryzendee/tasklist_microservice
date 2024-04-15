package com.app.taskservice.utils.sender;

import com.app.rabbit.mail.TaskEmailDetails;

public interface MailQueueSender {

    void send(TaskEmailDetails taskEmailDetails);
}
