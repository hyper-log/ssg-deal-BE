package on.ssgdeal.notification_service.infrastructure.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.core.EventEnvelope;
import on.ssgdeal.common.messaging.domain.enums.Topic;
import on.ssgdeal.notification_service.application.service.NotificationService;
import on.ssgdeal.notification_service.domain.enums.NotificationChannelType;
import on.ssgdeal.notification_service.infrastructure.messaging.dto.CreateNotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaEventListener {

    private final NotificationService notificationService;

    @KafkaListener(
        topics = {Topic.ORDER_SUCCESS_NOTIFICATION_EVENT},
        groupId = "on-ssgdeal-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenSuccessOrderEvent(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        Acknowledgment ack
    ) {
        try {
            log.info("메시지를 소비합니다. Topic: {}, message: {}", topic, message);
            EventEnvelope<CreateNotificationEvent> envelope =
                EventEnvelope.fromJson(message, CreateNotificationEvent.class);
            CreateNotificationEvent payload = envelope.payload();
            notificationService.sendNotification(payload.toDto(), NotificationChannelType.SLACK);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("메시지 소비 중 예외가 발생했습니다 : {}", e.getMessage());
            throw e;
        }
    }
}
