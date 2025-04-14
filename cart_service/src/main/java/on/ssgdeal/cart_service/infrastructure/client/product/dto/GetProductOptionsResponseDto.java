package on.ssgdeal.cart_service.infrastructure.client.product.dto;

import java.util.List;

public record GetProductOptionsResponseDto(
    Long productId,
    List<Option> options
) {

    public record Option(
        Long optionId,
        String optionName,
        Long extraPrice,
        Long productStock
    ) {

    }
}
