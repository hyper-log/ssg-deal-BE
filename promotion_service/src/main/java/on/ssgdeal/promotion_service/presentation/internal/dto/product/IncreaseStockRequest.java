package on.ssgdeal.promotion_service.presentation.internal.dto.product;

public record IncreaseStockRequest(
    Long productId,
    Long optionId,
    Long increaseStockAmount
) {

}
