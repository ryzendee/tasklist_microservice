package com.app.authservice.unit.utils.sender;

import com.app.authservice.enums.ValidAuthData;
import com.app.authservice.models.mail.UserEmailDetails;
import com.app.authservice.utils.sender.mail.MailQueueSender;
import com.app.authservice.utils.sender.mail.MailQueueSenderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailQueueSenderTest {
    private static final String VALID_EMAIL = ValidAuthData.EMAIL.getValue();
    private static final String VALID_EXCHANGE = "exchange";
    private static final String VALID_ROUTING = "routing";

    private MailQueueSender mailQueueSender;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        mailQueueSender = new MailQueueSenderImpl(rabbitTemplate, VALID_EXCHANGE, VALID_ROUTING);
    }

    @Test
    void constructor_validArgs_shouldCreate() {
        RabbitTemplate mockTemplate = Mockito.mock(RabbitTemplate.class);

        assertThatCode(() -> new MailQueueSenderImpl(mockTemplate, VALID_EXCHANGE, VALID_ROUTING))
                .doesNotThrowAnyException();
    }

    @MethodSource("getInvalidArgsForConstructor")
    @ParameterizedTest
    void constructor_invalidArgs_throwIllegalArgEx(RabbitTemplate rabbitTemplate,
                                                   String exchange,
                                                   String routingKey) {
        assertThatThrownBy(() -> new MailQueueSenderImpl(rabbitTemplate, exchange, routingKey))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> getInvalidArgsForConstructor() {
        RabbitTemplate mockTemplate = Mockito.mock(RabbitTemplate.class);
        return Stream.of(
                //Scenario 1: null rabbitTemplate
                Arguments.of(null, VALID_EXCHANGE, VALID_ROUTING),

                //Scenario 2: blank exchange
                Arguments.of(mockTemplate, "   ", VALID_ROUTING),

                //Scenario 3: null exchange
                Arguments.of(mockTemplate, null, VALID_ROUTING),

                //Scenario 4: blank routingKey
                Arguments.of(mockTemplate, VALID_EXCHANGE, "   "),

                //Scenario 5: null routingKey
                Arguments.of(mockTemplate, VALID_EXCHANGE, null)
        );
    }

    @Test
    void sendMailToQueue_hz() {
        UserEmailDetails userEmailDetails = new UserEmailDetails(VALID_EMAIL);

        doNothing()
                .when(rabbitTemplate).convertAndSend(VALID_EXCHANGE, VALID_ROUTING, userEmailDetails);

        mailQueueSender.sendMailToQueue(userEmailDetails);

        verify(rabbitTemplate).convertAndSend(VALID_EXCHANGE, VALID_ROUTING, userEmailDetails);
    }
}
