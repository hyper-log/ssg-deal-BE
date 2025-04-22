package on.ssgdeal.order_service.infrastructure.messaging.producer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.entity.Outbox.AggregateType;
import on.ssgdeal.common.messaging.producer.KafkaOutboxProducer;
import on.ssgdeal.order_service.infrastructure.persistence.jpa.OutboxRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaOutboxProducer {

    private final KafkaOutboxProducer kafkaOutboxProducer;
    private final OutboxRepository outboxRepository;

    @Scheduled(fixedDelay = 1000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execOutboxPublishing() {
        log.info("Order Service - execOutboxPublishing");
        List<Outbox> outboxeList = outboxRepository.findByAggregateType(AggregateType.ORDER);
        if (outboxeList.isEmpty()) {
            log.info("no outboxes found");
            return;
        }
        outboxeList.forEach(kafkaOutboxProducer::publishOutboxMessage);
    }
}
