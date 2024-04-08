package com.app.authservice.unit.service;

import com.app.authservice.entity.AuthUser;
import com.app.authservice.enums.ValidAuthData;
import com.app.authservice.exception.custom.MessageSendingException;
import com.app.authservice.mapper.AuthUserToUserEmailDetailsMap;
import com.app.authservice.models.mail.UserEmailDetails;
import com.app.authservice.service.sender.SenderServiceImpl;
import com.app.authservice.utils.sender.mail.MailQueueSender;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SenderServiceTest {

    private static final String VALID_EMAIL = ValidAuthData.EMAIL.getValue();
    @InjectMocks
    private SenderServiceImpl senderService;

    @Mock
    private EmailValidator emailValidator;
    @Mock
    private MailQueueSender mailQueueSender;
    @Mock
    private AuthUserToUserEmailDetailsMap authUserToUserEmailDetailsMap;
    @Mock
    private AuthUser authUser;

    @Test
    void sendWelcomeMailMessage_validEmail_shouldSend() {
        UserEmailDetails userEmailDetails = getUserEmailDetailsWithValidEmail();

        when(authUser.getEmail())
                .thenReturn(VALID_EMAIL);
        when(emailValidator.isValid(VALID_EMAIL))
                .thenReturn(true);
        when(authUserToUserEmailDetailsMap.map(authUser))
                .thenReturn(userEmailDetails);
        doNothing().when(mailQueueSender)
                .sendMailToQueue(userEmailDetails);

        senderService.sendWelcomeMailMessage(authUser);

        verify(authUser, atLeastOnce()).getEmail();
        verify(emailValidator).isValid(VALID_EMAIL);
        verify(authUserToUserEmailDetailsMap).map(authUser);
        verify(mailQueueSender).sendMailToQueue(userEmailDetails);
    }

    private UserEmailDetails getUserEmailDetailsWithValidEmail() {
        return new UserEmailDetails(VALID_EMAIL);
    }

    @Test
    void sendWelcomeMailMessage_invalidEmail_throwsMessageSendingEx() {
        String invalidEmail = "test-email";
        int getEmailNumOfInvocations = 2;

        when(authUser.getEmail())
                .thenReturn(invalidEmail);
        when(emailValidator.isValid(invalidEmail))
                .thenReturn(false);

        assertThatThrownBy(() -> senderService.sendWelcomeMailMessage(authUser))
                .isInstanceOf(MessageSendingException.class);

        verify(authUser, atLeastOnce()).getEmail();
        verify(emailValidator).isValid(invalidEmail);
    }

    @Test
    void sendWelcomeMessage_queueSenderThrowsAmqpEx_senderDontThrowEx() {
        UserEmailDetails userEmailDetails = getUserEmailDetailsWithValidEmail();
        String exMessage = "test-message";

        when(authUser.getEmail())
                .thenReturn(VALID_EMAIL);
        when(emailValidator.isValid(VALID_EMAIL))
                .thenReturn(true);
        when(authUserToUserEmailDetailsMap.map(authUser))
                .thenReturn(userEmailDetails);
        doThrow(new AmqpException(exMessage))
                .when(mailQueueSender).sendMailToQueue(userEmailDetails);

        assertThatCode(() -> senderService.sendWelcomeMailMessage(authUser))
                .doesNotThrowAnyException();

        verify(authUser, atLeastOnce()).getEmail();
        verify(emailValidator).isValid(VALID_EMAIL);
        verify(authUserToUserEmailDetailsMap).map(authUser);
        verify(mailQueueSender).sendMailToQueue(userEmailDetails);

    }

}
