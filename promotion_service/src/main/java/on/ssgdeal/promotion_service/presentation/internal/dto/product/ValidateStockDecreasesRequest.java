package on.ssgdeal.promotion_service.presentation.internal.dto.product;

import java.util.List;

public record ValidateStockDecreasesRequest(
    List<ProductDetail> getProductDetails
) {

    public record ProductDetail(
        Long productId,
        Long optionId,
        Long decreaseStockAmount
    ) {

    }

}
