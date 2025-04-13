package on.ssgdeal.cart_service.application.service.dto;

public record UpdateCartProductRequestDto(
    Long userId,
    Long productId,
    Long optionId,
    Long quantity,
    Long afterOptionId,
    Long afterQuantity
) {

}
