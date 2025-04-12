package on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos;

public record DecreaseProductStockResponseDto(Long productId, Long optionId,
                                              Long decreaseStockAmount) {

    public static DecreaseProductStockResponseDto from(Long productId, Long optionId,
        Long decreaseStockAmount) {
        return new DecreaseProductStockResponseDto(productId, optionId, decreaseStockAmount);
    }

}
