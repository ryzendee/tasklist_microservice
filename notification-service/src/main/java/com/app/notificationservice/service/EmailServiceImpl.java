package com.app.notificationservice.service;

import com.app.rabbit.mail.TaskEmailDetails;
import com.app.rabbit.mail.UserEmailDetails;
import com.app.notificationservice.exception.custom.CreatingMessageException;
import com.app.notificationservice.exception.custom.SendMessageException;
import com.app.notificationservice.factory.email.EmailMessageFactory;
import com.app.notificationservice.models.EmailMessage;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailMessageFactory emailMessageFactory;
    @Override
    public void sendWelcomeMessage(UserEmailDetails userEmailDetails) throws CreatingMessageException{
        try {
            EmailMessage emailMessage = emailMessageFactory.createWelcomeMessage(userEmailDetails);
            createMessageAndSend(emailMessage);
        } catch (IOException | MessagingException ex) {
            log.error("Failed to send welcome email message", ex);
            throw new SendMessageException("Cannot send email message");
        }
    }

    @Override
    public void sendTaskReportMessage(TaskEmailDetails taskEmailDetails) throws CreatingMessageException {
        try {
            EmailMessage emailMessage = emailMessageFactory.createTaskReportMessage(taskEmailDetails);
            createMessageAndSend(emailMessage);
        } catch (IOException | TemplateException | MessagingException ex) {
            log.error("Failed to send task report email message", ex);
            throw new SendMessageException("Cannot send email message");
        }
    }

    private void createMessageAndSend(EmailMessage emailMessage) throws MessagingException {
        MimeMessage message = createMimeMessage(emailMessage.getSubject(), emailMessage.getRecipientEmail(), emailMessage.getText());
        javaMailSender.send(message);
    }

    private MimeMessage createMimeMessage(String subject, String to, String text) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        return mimeMessage;
    }
}
