package on.ssgdeal.cart_service.application.service.dto;

import java.util.List;

public record DeleteCartProductRequestDto(
    Long userId,
    List<ProductOption> productOptions
) {

    public record ProductOption(
        Long productId,
        Long optionId
    ) {

    }
}
