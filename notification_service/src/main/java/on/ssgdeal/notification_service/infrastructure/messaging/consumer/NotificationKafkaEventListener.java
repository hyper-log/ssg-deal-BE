package on.ssgdeal.notification_service.infrastructure.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.mdc.MdcContext;
import on.ssgdeal.common.mdc.PassportMdcContext;
import on.ssgdeal.common.messaging.core.EventEnvelope;
import on.ssgdeal.common.messaging.domain.enums.Topic;
import on.ssgdeal.common.messaging.exception.NonRecoverableException;
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
    private final PassportUtil passportUtil;

    @KafkaListener(
        topics = {Topic.ORDER_SUCCESS_NOTIFICATION_EVENT},
        groupId = "on-ssgdeal-group-order-success-notification",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenSuccessOrderEvent(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
        Acknowledgment ack
    ) throws Exception {
        log.info("메시지를 소비합니다. Topic : {}, Partition: {}, Offset: {}, Message key :{}",
            topic, partition, offset, key);
        EventEnvelope<CreateNotificationEvent> envelope =
            EventEnvelope.fromJson(message, CreateNotificationEvent.class);

        String passportId = envelope.passportId();
        try (
            MdcContext mdcContext = new MdcContext();
            PassportMdcContext passportMdcContext = new PassportMdcContext(passportUtil, passportId);
        ) {
            consumeSuccessOrderEvent(envelope.payload());
            ack.acknowledge();
        } catch (NonRecoverableException nre) {
            log.error("재시도하지 않을 예외가 발생했습니다. => {}", nre.getMessage());
            throw nre;
        } catch (Exception e) {
            log.error("재시도할 예외가 발생했습니다.", e);
            throw e;
        }
    }

    private void consumeSuccessOrderEvent(CreateNotificationEvent payload) {
        try {
            notificationService.sendNotification(payload.toDto(), NotificationChannelType.SLACK);
        } catch (Exception e) {
            log.error("메시지 소비에 실패했습니다.", e);
            throw e;
        }
    }
}
