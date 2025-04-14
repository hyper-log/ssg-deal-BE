package on.ssgdeal.promotion_service.presentation.internal.dto.product;

import java.util.List;

public record GetProductDetailsRequest(
    List<ProductDetail> productDetails
) {

    public record ProductDetail(
        Long productId,
        Long optionId
    ) {

    }

}
