package on.ssgdeal.promotion_service.presentation.internal.dto.product;

public record DecreaseStockRequest(
    Long productId,
    Long optionId,
    Long decreaseStockAmount
) {

}
