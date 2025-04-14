package on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos;

public record InCreaseProductStockRequestDto(
    Long productId,
    Long optionId,
    Long increaseStockAmount
) {

    public static InCreaseProductStockRequestDto from(
        Long productId,
        Long optionId,
        Long increaseStockAmount
    ) {
        return new InCreaseProductStockRequestDto(productId, optionId, increaseStockAmount);
    }

}
