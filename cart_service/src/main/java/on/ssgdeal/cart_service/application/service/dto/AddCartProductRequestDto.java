package on.ssgdeal.cart_service.application.service.dto;

import on.ssgdeal.cart_service.infrastructure.client.product.dto.IsProductStockAvailableRequestDto;

public record AddCartProductRequestDto(
    Long userId,
    Long productId,
    Long optionId,
    Long quantity
) {

    public IsProductStockAvailableRequestDto toDto() {
        return new IsProductStockAvailableRequestDto(
            productId,
            optionId,
            quantity
        );
    }
}
