package on.ssgdeal.promotion_service.presentation.internal.dto.product;

import java.util.List;
import lombok.Builder;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;

@Builder
public record GetProductDetailsResponse(
    List<ProductDetail> productDetails
) {

    @Builder
    public record ProductDetail(
        PromotionStatus promotionStatus,
        Long companyId,
        String companyName,
        Long productId,
        String productName,
        String productPreviewImgUrl,
        Long originalPrice,
        Long promotionPrice,
        Long optionId,
        String optionName,
        Long extraPrice
    ) {

    }

}
