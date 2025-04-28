package on.ssgdeal.promotion_service.application.service.dto.product;

import java.util.List;
import lombok.Builder;

@Builder
public record ValidateStockDecreasesRequestDto(
    List<ProductDetail> getProductDetails
) {

    @Builder
    public record ProductDetail(
        Long productId,
        Long optionId,
        Long decreaseStockAmount
    ) {

    }

}
