package on.ssgdeal.promotion_service.infrastructure.messaging.dtos;

import on.ssgdeal.common.messaging.core.EventPayload;
import on.ssgdeal.promotion_service.application.service.dto.product.IncreaseStockRequestDto;

public record IncreaseStockEvent(
    Long productId,
    Long optionId,
    Long increaseStockAmount
) implements EventPayload {

    public IncreaseStockRequestDto toDto() {
        return new IncreaseStockRequestDto(
            productId,
            optionId,
            increaseStockAmount
        );
    }
}
