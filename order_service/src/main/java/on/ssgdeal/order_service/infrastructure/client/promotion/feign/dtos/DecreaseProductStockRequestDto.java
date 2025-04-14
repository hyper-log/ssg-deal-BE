package on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos;

public record DecreaseProductStockRequestDto(
    Long productId,
    Long optionId,
    Long decreaseStockAmount
) {

    public static DecreaseProductStockRequestDto from(
        Long productId,
        Long optionId,
        Long decreaseStockAmount
    ) {
        return new DecreaseProductStockRequestDto(productId, optionId, decreaseStockAmount);
    }

}
