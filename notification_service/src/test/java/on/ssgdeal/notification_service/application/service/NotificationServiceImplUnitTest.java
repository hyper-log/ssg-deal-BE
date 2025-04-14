package on.ssgdeal.notification_service.application.service;

import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.entity.dto.CreateNotificationDto;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.infrastructure.client.slack.converter.SlackTimestampToKSTConverter;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplUnitTest {

    @Mock
    private SlackClient slackClient;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTemplateRepository notificationTemplateRepository;

    @Mock
    private SlackTimestampToKSTConverter slackTimestampConverter;

    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    private CreateNotificationRequestDto mockRequestDto;

    private NotificationTemplate mockTemplate;

    private String mockTimestamp;
    private LocalDateTime mockSendAt;

    private Notification mockNotification;

    private String mockContent;
    private CreateNotificationDto mockDto;


    @BeforeEach
    void setUp() {
        mockRequestDto = CreateNotificationRequestDto.builder()
                .receiverSlackEmail("hyunj2034@naver.com")
                .senderSlackEmail("master@naver.com")
                .ordererName("양현진")
                .totalOrderId(123L)
                .paymentPrice(10000L)
                .orderAt(LocalDate.now())
                .orderStatus("결제완료")
                .build();

        mockTemplate = NotificationTemplate.builder()
                .type(NotificationTemplateType.ORDER_COMPLETED)
                .content("주문자: {ordererName}\n주문번호: {orderId}\n결제금액: {paymentPrice}\n주문상태: {orderStatus}")
                .build();

        mockContent = "주문자: 양현진\n주문번호: 123\n결제금액: 10000\n주문상태: ORDER_COMPLETED";

        mockTimestamp = "1744109481.123456";

        mockSendAt = LocalDateTime.of(2025, 4, 8, 19, 51, 21, 123456000);

        mockDto = CreateNotificationDto.builder()
                .requestDto(mockRequestDto)
                .template(mockTemplate)
                .sendAt(mockSendAt)
                .content(mockContent)
                .build();

        mockNotification = Notification.create(mockDto);

    }

    @Test
    @DisplayName("주문 완료 알림 전송 성공")
    void sendSlackNotificationOrderCompletedSuccess() {

        // given
        when(notificationTemplateRepository.findByType(NotificationTemplateType.ORDER_COMPLETED))
                .thenReturn(Optional.of(mockTemplate));
        when(slackClient.sendNotificationToUser(anyString(), anyString()))
                .thenReturn(mockTimestamp);
        when(slackTimestampConverter.convertToKST(anyString()))
                .thenReturn(mockSendAt);
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                            Notification savedNotification = invocation.getArgument(0);
                            Field idField = Notification.class.getDeclaredField("id");
                            idField.setAccessible(true);
                            idField.set(savedNotification, 1L);
                            return savedNotification;
                        }
                );

        // when
        CreateNotificationResponseDto response = notificationServiceImpl.sendSlackNotification(mockRequestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.notificationId()).isEqualTo(1L);
        verify(notificationTemplateRepository, times(1))
                .findByType(NotificationTemplateType.ORDER_COMPLETED);
        verify(notificationRepository, times(1))
                .save(any(Notification.class));
    }
}