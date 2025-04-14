package on.ssgdeal.cart_service.infrastructure.client.product.dto;

public record IsProductStockAvailableRequestDto(
    Long productId,
    Long optionId,
    Long quantity
) {

}
