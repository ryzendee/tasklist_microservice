package com.app.authservice.unit.service;

import com.app.authservice.exception.custom.MessageSendingException;
import com.app.authservice.factory.MailMessageFactory;
import com.app.authservice.models.MailMessage;
import com.app.authservice.service.sender.SenderService;
import com.app.authservice.service.sender.SenderServiceImpl;
import com.app.authservice.utils.sender.mail.MailQueueSenderImpl;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SenderServiceTest {


    @InjectMocks
    private SenderServiceImpl senderService;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private MailQueueSenderImpl mailQueueSender;

    @Test
    void sendWelcomeMailMessage_validMail_ok() {
        String validEmail = "test@gmail.com";

        when(emailValidator.isValid(validEmail))
                .thenReturn(true);
        doNothing()
                .when(mailQueueSender).sendMailToQueue(any(MailMessage.class));

        senderService.sendWelcomeMailMessage(validEmail);

        verify(emailValidator).isValid(validEmail);
        verify(mailQueueSender).sendMailToQueue(any(MailMessage.class));
    }

    @ValueSource(strings = {"invalid-email"})
    @ParameterizedTest
    void sendWelcomeMailMessage_invalidEmailFormat_throwMessageSendEx(String invalidEmail) {
        when(emailValidator.isValid(invalidEmail))
                .thenReturn(false);

        assertThatThrownBy(() -> senderService.sendWelcomeMailMessage(invalidEmail))
                .isInstanceOf(MessageSendingException.class);

        verify(emailValidator).isValid(invalidEmail);
    }


    @NullSource
    @EmptySource
    @ParameterizedTest
    void sendWelcomeMailMessage_nullAndEmptyEmail_throwMessageSendEx(String invalidEmail) {
        assertThatThrownBy(() -> senderService.sendWelcomeMailMessage(invalidEmail))
                .isInstanceOf(MessageSendingException.class);
    }
}
