package on.ssgdeal.promotion_service.application.service.dto.product;

public record DecreaseStockRequestDto(
    Long productId,
    Long optionId,
    Long decreaseStockAmount
) {

}
