package on.ssgdeal.common.messaging.producer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.config.KafkaConsumerConfig;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOutboxProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void publishOutboxMessages(List<Outbox> outboxeList) {
        List<CompletableFuture<Outbox>> completableFutures = outboxeList.stream()
            .map(this::publishOutboxMessage)
            .toList();

        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        } catch (Exception e) {
            log.error("메시지 발행 중 오류가 발생했습니다.", e);
        }
        List<Outbox> completedOutbox = completableFutures.stream()
            .map(CompletableFuture::join)
            .toList();
        log.info("메시지 발행 완료. => size: {}", completedOutbox.size());
    }

    private CompletableFuture<Outbox> publishOutboxMessage(Outbox outbox) {
        String topic = outbox.getTopic();
        String key = String.valueOf(outbox.getAggregateId());
        String payload = outbox.getPayload();

        return kafkaTemplate.send(topic, key, payload)
            .handle((res, ex) -> {
                if (ex != null) {
                    log.error("메시지 발행을 실패했습니다. => id: {}, retryCount: {}",
                        outbox.getId(), outbox.getRetryCount());
                    outbox.increaseRetryCount();
                    if (outbox.isOverRetryCount()) {
                        log.error("재시도 횟수를 초과하여 메시지 발행을 실패했습니다. => id: {}",
                            outbox.getId());
                        kafkaTemplate.send(topic + KafkaConsumerConfig.DLT_SUFFIX, key, payload);
                    }
                } else {
                    outbox.success();
                }
                return outbox;
            });
    }
}