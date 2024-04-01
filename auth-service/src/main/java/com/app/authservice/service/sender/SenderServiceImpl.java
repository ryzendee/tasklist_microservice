package com.app.authservice.service.sender;

import com.app.authservice.exception.MailMessageException;
import com.app.authservice.factory.MailMessageFactory;
import com.app.authservice.models.MailMessage;
import com.app.authservice.utils.sender.mail.MailQueueSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderServiceImpl implements SenderService {

    private final MailQueueSender mailQueueSender;

    @Override
    public void sendWelcomeMailMessage(String reciever) {
        if (StringUtils.isBlank(reciever)) {
            throw new MailMessageException("Reciever must not be null or blank: " + reciever);
        }

        log.info("Sending mail to reciever: {}", reciever);
        MailMessage mailMessage = MailMessageFactory.createWelcomeMailMessage(reciever);
        mailQueueSender.sendMailToQueue(mailMessage);
    }
}
