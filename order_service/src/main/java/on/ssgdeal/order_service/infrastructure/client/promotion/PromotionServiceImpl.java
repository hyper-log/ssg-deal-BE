package on.ssgdeal.order_service.infrastructure.client.promotion;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.mdc.MdcKey;
import on.ssgdeal.common.messaging.core.EventEnvelope;
import on.ssgdeal.common.messaging.domain.entity.Outbox;
import on.ssgdeal.common.messaging.domain.entity.Outbox.AggregateType;
import on.ssgdeal.common.messaging.domain.enums.Topic;
import on.ssgdeal.common.messaging.domain.repository.OutboxRepository;
import on.ssgdeal.order_service.application.service.PromotionService;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.PromotionServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.IncreaseStockEvent;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PromotionServiceImpl")
public class PromotionServiceImpl implements PromotionService {

    private final PromotionServiceFeignClient feignClient;
    private final OutboxRepository outboxRepository;

    @Override
    public GetProductInfoDto getProductInfoAndStockDecrease(GetProductInfoRequestDto requestDto) {
        log.info("getProductInfoAndStockDecrease : {}", requestDto.toString());
        var getProductInfoDto = feignClient.getProductInfoAndStockDecrease(requestDto);
        return getProductInfoDto.data();
    }

    @Override
    public InCreaseProductStockResponseDto increaseProductStock(
        InCreaseProductStockRequestDto requestDto) {
        log.info("increaseProductStock : {}", requestDto.toString());
        var inCreaseProductStockResponseDto = feignClient.increaseStock(requestDto);
        return inCreaseProductStockResponseDto.data();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishIncreaseProductStockMessage(
        Long totalOrderId,
        List<IncreaseStockEvent> payloadList
    ) {
        log.info("sendIncreaseProductStockMessage : {} - {}", totalOrderId, payloadList);
        String passportId = MDC.get(MdcKey.PASSPORT_ID.getKey());
        List<Outbox> outboxList = payloadList.stream()
            .map(payload -> {
                EventEnvelope<IncreaseStockEvent> envelope = EventEnvelope.wrap(
                    Topic.INCREASE_STOCK_EVENT, passportId, payload);
                return Outbox.create(
                    envelope.topic(),
                    AggregateType.ORDER,
                    totalOrderId,
                    envelope.toJson()
                );
            }).toList();
        outboxRepository.saveAll(outboxList);
    }

    @Override
    public DecreaseProductStockResponseDto decreaseProductStock(
        DecreaseProductStockRequestDto requestDto) {
        log.info("decreaseProductStock : {}", requestDto.toString());
        var decreaseProductStockResponseDto = feignClient.decreaseStock(requestDto);
        return decreaseProductStockResponseDto.data();
    }
}
