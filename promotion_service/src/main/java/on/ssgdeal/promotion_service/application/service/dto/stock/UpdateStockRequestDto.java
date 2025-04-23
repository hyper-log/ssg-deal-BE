package on.ssgdeal.promotion_service.application.service.dto.stock;

public record UpdateStockRequestDto(
    Long productId,
    Long optionId,
    Long amount
) {

}
