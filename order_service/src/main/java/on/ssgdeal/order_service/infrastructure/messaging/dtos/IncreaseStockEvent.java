package on.ssgdeal.order_service.infrastructure.messaging.dtos;


import on.ssgdeal.common.messaging.core.EventPayload;

public record IncreaseStockEvent(
    Long productId,
    Long optionId,
    Long increaseStockAmount
) implements EventPayload {

    public static IncreaseStockEvent from(
        Long productId,
        Long optionId,
        Long increaseStockAmount
    ) {
        return new IncreaseStockEvent(productId, optionId, increaseStockAmount);
    }

}
