package com.app.authservice.service.sender;

import com.app.authservice.exception.custom.MessageSendingException;
import com.app.authservice.factory.MailMessageFactory;
import com.app.authservice.models.MailMessage;
import com.app.authservice.utils.sender.mail.MailQueueSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderServiceImpl implements SenderService {

    private final EmailValidator emailValidator;
    private final MailQueueSender mailQueueSender;

    @Override
    public void sendWelcomeMailMessage(String recieverEmail) {
        if (StringUtils.isBlank(recieverEmail)) {
            log.error("Email blank or empty: {}", recieverEmail);
            throw new MessageSendingException("Email must not be null or blank: " + recieverEmail);
        }

        if (!emailValidator.isValid(recieverEmail)) {
            log.error("Invalid email format: {}", recieverEmail);
            throw new MessageSendingException("Invalid email format");
        }

        log.info("Sending mail to reciever: {}", recieverEmail);
        MailMessage mailMessage = MailMessageFactory.createWelcomeMailMessage(recieverEmail);
        mailQueueSender.sendMailToQueue(mailMessage);
    }
}
