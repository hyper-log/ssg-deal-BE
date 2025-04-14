package on.ssgdeal.promotion_service.application.service.dto.product;

import java.util.List;

public record ValidateStockDecreasesRequestDto(
    List<ProductDetail> productDetails
) {

    public record ProductDetail(
        Long companyId,
        Long optionId,
        Long decreaseStockAmount
    ) {

    }

}
