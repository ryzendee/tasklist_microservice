package com.app.notificationservice.factory.email;

import com.app.notificationservice.enums.email.TaskReportModelKey;
import com.app.rabbit.mail.TaskEmailDetails;
import com.app.rabbit.mail.UserEmailDetails;
import com.app.notificationservice.enums.email.TemplatePath;
import com.app.notificationservice.enums.email.EmailSubject;
import com.app.notificationservice.models.EmailMessage;
import com.app.notificationservice.utils.template.FreemarkerTemplateProcessor;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailMessageFactoryImpl implements EmailMessageFactory{

    private final FreemarkerTemplateProcessor freemarkerTemplateProcessor;
    @Override
    public EmailMessage createWelcomeMessage(UserEmailDetails userEmailDetails) throws IOException {

        String content = freemarkerTemplateProcessor.getContentFromTemplate(TemplatePath.WELCOME_MESSAGE.getPath());

        return buildMessage(userEmailDetails.email(), EmailSubject.WELCOME_MESSAGE.getSubjectName(), content);
    }

    @Override
    public EmailMessage createTaskReportMessage(TaskEmailDetails taskEmailDetails) throws TemplateException, IOException {
        Map<String, String> objectModel = createObjectModelForTaskReport(taskEmailDetails);

        String content = freemarkerTemplateProcessor.getContentFromTemplateWithModel(TemplatePath.TASK_REPORT.getPath(), objectModel);

        return buildMessage(taskEmailDetails.email(), EmailSubject.WELCOME_MESSAGE.getSubjectName(), content);
    }


    private Map<String, String> createObjectModelForTaskReport(TaskEmailDetails taskEmailDetails) {
        Map<String, String> map = new HashMap<>();

        map.put(TaskReportModelKey.TOTAL_TASKS.getKey(), String.valueOf(taskEmailDetails.allTasks()));
        map.put(TaskReportModelKey.COMPLETED_TASKS.getKey(), String.valueOf(taskEmailDetails.completedTasks()));
        map.put(TaskReportModelKey.TASKS_IN_PROCESS.getKey(), String.valueOf(taskEmailDetails.tasksInProcess()));

        return map;
    }

    private EmailMessage buildMessage(String recipientEmail, String subject, String text) {
        return EmailMessage.builder()
                .recipientEmail(recipientEmail)
                .subject(subject)
                .text(text)
                .build();
    }
}
