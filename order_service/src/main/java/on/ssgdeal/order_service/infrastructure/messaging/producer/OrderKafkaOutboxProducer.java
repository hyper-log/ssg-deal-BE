package on.ssgdeal.order_service.infrastructure.messaging.producer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.entity.Outbox.AggregateType;
import on.ssgdeal.common.messaging.domain.repository.OutboxRepository;
import on.ssgdeal.common.messaging.producer.KafkaOutboxProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaOutboxProducer {

    private final KafkaOutboxProducer kafkaOutboxProducer;
    private final OutboxRepository outboxRepository;

    @Scheduled(fixedDelay = 60000)
    public void execOutboxPublishing() {
        log.info("Order Service - execOutboxPublishing");
        List<Outbox> outboxeList = outboxRepository.findByAggregateType(AggregateType.ORDER);
        if (outboxeList.isEmpty()) {
            log.info("no outboxes found");
            return;
        }
        kafkaOutboxProducer.publishOutboxMessages(outboxeList);
    }
}
