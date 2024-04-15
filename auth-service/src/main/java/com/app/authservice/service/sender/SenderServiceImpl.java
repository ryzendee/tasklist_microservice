package com.app.authservice.service.sender;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.exception.custom.MessageSendingException;
import com.app.authservice.mapper.AuthUserToUserEmailDetailsMap;
import com.app.rabbit.mail.UserEmailDetails;
import com.app.authservice.utils.sender.mail.MailQueueSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderServiceImpl implements SenderService {

    private final EmailValidator emailValidator;
    private final MailQueueSender mailQueueSender;
    private final AuthUserToUserEmailDetailsMap authUserToUserEmailDetailsMap;

    @Override
    public void sendWelcomeMailMessage(AuthUser authUser) {
        if (!emailValidator.isValid(authUser.getEmail())) {
            throw new MessageSendingException("Invalid user email: " + authUser.getEmail());
        }

        UserEmailDetails userEmailDetails = authUserToUserEmailDetailsMap.map(authUser);

        try {
            mailQueueSender.sendMailToQueue(userEmailDetails);
        } catch (AmqpException ex) {
            log.error("Failed to send welcome message: {}", userEmailDetails.email(), ex);
            return;
        }

        log.info("Sent details for welcome mail message to the queue: {}", userEmailDetails);
    }
}
