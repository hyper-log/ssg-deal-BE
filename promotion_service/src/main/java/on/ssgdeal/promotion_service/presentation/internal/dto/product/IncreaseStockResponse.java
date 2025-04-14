package on.ssgdeal.promotion_service.presentation.internal.dto.product;

import lombok.Builder;

@Builder
public record IncreaseStockResponse(
    Long productId,
    Long optionId,
    Long increaseStockAmount
) {

}
