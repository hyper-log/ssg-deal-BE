package on.ssgdeal.cart_service.infrastructure.client.product.feign.dto;

import java.util.List;

public record GetProductOptionsResponse(
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
