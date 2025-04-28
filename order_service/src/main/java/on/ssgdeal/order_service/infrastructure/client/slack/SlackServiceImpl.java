package on.ssgdeal.order_service.infrastructure.client.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.mdc.MdcKey;
import on.ssgdeal.common.messaging.core.EventEnvelope;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.entity.Outbox.AggregateType;
import on.ssgdeal.common.messaging.domain.enums.Topic;
import on.ssgdeal.common.messaging.domain.repository.OutboxRepository;
import on.ssgdeal.order_service.application.service.SlackService;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.SlackServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackResponseDto;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.OrderSuccessNotificationEvent;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SlackServiceImpl")
public class SlackServiceImpl implements SlackService {

    private final SlackServiceFeignClient feignClient;
    private final OutboxRepository outboxRepository;

    @Override
    public OrderCompleteSendSlackResponseDto sendOrderCompleteMessage(
        OrderCompleteSendSlackRequestDto requestDto) {
        log.info("sendOrderCompleteMessage: {}", requestDto);
        var orderCompleteSendSlackResponseDto = feignClient.sendOrderCompleteMessage(requestDto);
        return orderCompleteSendSlackResponseDto.data();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishCompletedOrderSlackMessage(
        Long totalOrderId,
        OrderSuccessNotificationEvent payload
    ) {
        log.info("publishCompletedOrderSlackMessage: {} - {}", totalOrderId, payload);
        String passportId = MDC.get(MdcKey.PASSPORT_ID.getKey());
        EventEnvelope<OrderSuccessNotificationEvent> envelope = EventEnvelope.wrap(
            Topic.ORDER_SUCCESS_NOTIFICATION_EVENT, passportId, payload);
        Outbox outbox = Outbox.create(
            envelope.topic(),
            AggregateType.ORDER,
            totalOrderId,
            envelope.toJson()
        );
        outboxRepository.save(outbox);
    }
}
