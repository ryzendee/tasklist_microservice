package com.app.notificationservice.factory.email;

import com.app.rabbit.mail.TaskEmailDetails;
import com.app.rabbit.mail.UserEmailDetails;
import com.app.notificationservice.models.EmailMessage;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface EmailMessageFactory {

    EmailMessage createWelcomeMessage(UserEmailDetails userEmailDetails) throws IOException;

    EmailMessage createTaskReportMessage(TaskEmailDetails taskEmailDetails) throws TemplateException, IOException;
}
