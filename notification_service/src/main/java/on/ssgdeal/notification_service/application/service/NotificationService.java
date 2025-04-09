package on.ssgdeal.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.infrastructure.client.slack.converter.SlackTimestampToKSTConverter;
import on.ssgdeal.notification_service.presentation.internal.dto.CreateNotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j(topic = "NotificationService")
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SlackClient slackClient;
    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final SlackTimestampToKSTConverter slackTimestampToKSTConverter;
    @Transactional
    public CreateNotificationResponse sendSlackNotification(final CreateNotificationRequestDto requestDto) {
        log.info("주문 완료 슬랙 메시지 전송 요청 : {}", requestDto);
        NotificationTemplate template = getOrThrowNotificationTemplate(NotificationTemplateType.ORDER_COMPLETED);
        String content = createOrderCompletedSlackNotification(requestDto, template.getContent());
        String timestamp = slackClient.sendNotificationToUser(requestDto.receiverSlackEmail(), content);

        LocalDateTime sendAt = slackTimestampToKSTConverter.convertToKST(timestamp);
        Notification notification = Notification.create(requestDto, content, template, sendAt);
        notificationRepository.save(notification);
        return CreateNotificationResponse.from(notification);
    }
    public String createOrderCompletedSlackNotification(CreateNotificationRequestDto requestDto, String content) {
        return content
                .replace("{ordererName}", requestDto.ordererName())
                .replace("{orderId}", String.valueOf(requestDto.orderId()))
                .replace("{paymentPrice}", String.valueOf(requestDto.paymentPrice()))
                .replace("{orderAt}", requestDto.orderAt().toString())
                .replace("{orderStatus}", requestDto.orderStatus());
    }
    private NotificationTemplate getOrThrowNotificationTemplate(NotificationTemplateType type) {
        return notificationTemplateRepository
                .findByType(type)
                .orElseThrow(() -> new RuntimeException("알림 템플릿이 존재하지 않습니다."));
    }
}
