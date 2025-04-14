package on.ssgdeal.cart_service.presentation.external.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import on.ssgdeal.cart_service.application.service.dto.AddCartProductRequestDto;

public record AddCartProductRequest(
    @NotNull Long productId,
    @NotNull Long optionId,
    @NotNull @Positive Long quantity
) {

    public AddCartProductRequestDto toDto(Long userId) {
        return new AddCartProductRequestDto(userId, productId, optionId, quantity);
    }
}
