package on.ssgdeal.promotion_service.application.service.dto.product;

import java.util.List;

public record GetProductDetailsRequestDto(
    List<ProductDetail> productDetails
) {

    public record ProductDetail(
        Long productId,
        Long optionId
    ) {

    }

}
