package on.ssgdeal.cart_service.presentation.external.dto;

import jakarta.validation.constraints.NotNull;
import on.ssgdeal.cart_service.application.service.dto.UpdateCartProductRequestDto;

public record UpdateCartProductRequest(
    @NotNull Long productId,
    @NotNull Long optionId,
    @NotNull Long quantity,
    Long afterOptionId,
    Long afterQuantity
) {

    public UpdateCartProductRequestDto toDto(Long userId) {
        return new UpdateCartProductRequestDto(
            userId,
            productId,
            optionId,
            quantity,
            afterOptionId,
            afterQuantity
        );
    }
}
