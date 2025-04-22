package on.ssgdeal.common.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOutboxProducer {

    public static final String DLT_SUFFIX = ".dlt";

    private final KafkaTemplate<String, String> kafkaTemplate;

    // 단일 메시지 발행
    public void publishOutboxMessage(Outbox outbox) {
        String topic = outbox.getTopic();
        String key = String.valueOf(outbox.getAggregateId());
        String payload = outbox.getPayload();

        kafkaTemplate.send(topic, key, payload)
            .handle((res, ex) -> {
                if (ex != null) {
                    log.error("메시지 발행을 실패했습니다. => id: {}, retryCount: {}",
                        outbox.getId(), outbox.getRetryCount());
                    outbox.increaseRetryCount();
                    if (outbox.isOverRetryCount()) {
                        log.error("재시도 횟수를 초과하여 메시지 발행을 실패했습니다. => id: {}",
                            outbox.getId());
                        kafkaTemplate.send(topic + DLT_SUFFIX, key, payload);
                    }
                } else {
                    outbox.success();
                }
                return outbox;
            });
    }
}
