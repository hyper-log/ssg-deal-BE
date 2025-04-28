package on.ssgdeal.common.messaging.producer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.config.KafkaConsumerConfig;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.repository.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOutboxProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishOutboxMessages(List<Outbox> outboxeList) {
        List<CompletableFuture<Outbox>> completableFutures = outboxeList.stream()
            .map(this::publishOutboxMessage)
            .toList();

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                List<Outbox> updatedOutbox = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
                log.info("아웃박스 데이터 {}개의 메시지를 발행했습니다.", updatedOutbox.size());
                outboxRepository.saveAll(updatedOutbox);
            }).exceptionally(ex -> {
                log.error("아웃박스 메시지 발행 중 예외가 발생했습니다. {}", ex.getMessage());
                return null;
            });
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