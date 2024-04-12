package com.app.notificationservice.service;


import com.app.rabbit.mail.TaskEmailDetails;
import com.app.rabbit.mail.UserEmailDetails;

public interface EmailService {

    void sendWelcomeMessage(UserEmailDetails userEmailDetails);
    void sendTaskReportMessage(TaskEmailDetails taskEmailDetails);

}
