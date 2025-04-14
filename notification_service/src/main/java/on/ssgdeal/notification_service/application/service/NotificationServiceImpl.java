package on.ssgdeal.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.entity.dto.CreateNotificationDto;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.exception.NotificationException;
import on.ssgdeal.notification_service.infrastructure.client.slack.converter.SlackTimestampToKSTConverter;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j(topic = "NotificationServiceImpl")
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SlackClient slackClient;
    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final SlackTimestampToKSTConverter slackTimestampToKSTConverter;

    @Override
    @Transactional
    public CreateNotificationResponseDto sendSlackNotification(
            final CreateNotificationRequestDto requestDto
    ) {
        log.info("주문 완료 슬랙 메시지 전송 요청 : {}", requestDto);
        NotificationTemplate template = getOrThrowNotificationTemplate(NotificationTemplateType.ORDER_COMPLETED);
        String content = createOrderCompletedSlackNotification(requestDto, template.getContent());
        String timestamp = slackClient.sendNotificationToUser(requestDto.receiverSlackEmail(), content);
        LocalDateTime sendAt = slackTimestampToKSTConverter.convertToKST(timestamp);
        CreateNotificationDto notificationDto = CreateNotificationRequestDto.toDto(
                requestDto,
                content,
                template,
                sendAt
        );
        log.info("알림 생성 DTO : {}", notificationDto.toString());
        Notification notification = Notification.create(notificationDto);
        notificationRepository.save(notification);
        return CreateNotificationResponseDto.from(notification);
    }

    private String createOrderCompletedSlackNotification(
            final CreateNotificationRequestDto requestDto,
            final String content
    ) {
        return content
                .replace("{ordererName}", requestDto.ordererName())
                .replace("{orderId}", String.valueOf(requestDto.totalOrderId()))
                .replace("{paymentPrice}", String.valueOf(requestDto.paymentPrice()))
                .replace("{orderAt}", requestDto.orderAt().toString())
                .replace("{orderStatus}", requestDto.orderStatus());
    }

    private NotificationTemplate getOrThrowNotificationTemplate(final NotificationTemplateType type) {
        return notificationTemplateRepository
                .findByType(type)
                .orElseThrow(NotificationException.NotificationTemplateNotFoundException::new);
    }
}
