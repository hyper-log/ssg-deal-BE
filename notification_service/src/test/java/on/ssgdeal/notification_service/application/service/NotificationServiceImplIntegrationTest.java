package on.ssgdeal.notification_service.application.service;

import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.infrastructure.client.slack.converter.SlackTimestampToKSTConverter;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@Transactional
@SpringBootTest
@DisplayName("NotificationServiceImpl 통합 테스트")
public class NotificationServiceImplIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @MockitoBean
    private SlackClient slackClient;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockitoBean
    private SlackTimestampToKSTConverter slackTimestampToKSTConverter;

    private CreateNotificationRequestDto mockRequestDto;

    private NotificationTemplate mockTemplate;

    private String mockTimestamp;
    private LocalDateTime mockSendAt;


    @BeforeEach
    void setUp() {
        mockRequestDto = CreateNotificationRequestDto.builder()
                .receiverSlackEmail("hyunj2034@naver.com")
                .senderSlackEmail("master@naver.com")
                .ordererName("양현진")
                .totalOrderId(123L)
                .paymentPrice(10000L)
                .orderAt(LocalDate.of(2025,4,8))
                .orderStatus("결제완료")
                .build();

        mockTemplate = NotificationTemplate.builder()
                .type(NotificationTemplateType.ORDER_COMPLETED)
                .content("주문자: {ordererName}\n주문번호: {orderId}\n결제금액: {paymentPrice}\n주문날짜: {orderAt}\n주문상태: {orderStatus}")
                .build();

        mockTimestamp = "1744109481.123456";

        mockSendAt = LocalDateTime.of(2025, 4, 8, 19, 51, 21, 123456000);

        notificationTemplateRepository.save(mockTemplate);

    }

    @Nested
    @DisplayName("Describe: sendSlackNotification 메서드는")
    class sendSlackNotificationTest {

        @Nested
        @DisplayName("Context: 필수 입력과 슬랙 API가 성공했을 때")
        class sendSlackNotificationSuccessTest {

            @Test
            @DisplayName("It: 데이터를 저장한 후 notificationId를 반환한다.")
            void sendSlackNotificationTest() throws Exception {

                //given
                when(slackClient.sendNotificationToUser(anyString(), anyString())).thenReturn(mockTimestamp);
                when(slackTimestampToKSTConverter.convertToKST(anyString())).thenReturn(mockSendAt);

                //when
                CreateNotificationResponseDto response = notificationService.sendSlackNotification(mockRequestDto);

                //then
                assertThat(response).isNotNull();

                Notification notification = notificationRepository.findById(response.notificationId()).orElseThrow(
                        () -> new AssertionFailure("Notification이 저장되지 않았습니다.")
                );

                assertThat(response.notificationId()).isEqualTo(notification.getId());
            }
        }
    }
}