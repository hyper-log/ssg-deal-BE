package on.ssgdeal.promotion_service.application.service.dto.product;

public record IncreaseStockRequestDto(
    Long productId,
    Long optionId,
    Long increaseStockAmount
) {

}
