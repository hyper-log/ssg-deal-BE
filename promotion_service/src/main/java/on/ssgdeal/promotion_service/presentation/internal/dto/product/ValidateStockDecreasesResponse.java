package on.ssgdeal.promotion_service.presentation.internal.dto.product;

import java.util.List;
import lombok.Builder;

@Builder
public record ValidateStockDecreasesResponse(
    List<CompanyDetail> companyList
) {

    @Builder
    public record CompanyDetail(
        Long companyId,
        String companyName,
        List<CompanyProduct> companyProductList
    ) {

        @Builder
        public record CompanyProduct(
            String promotionStatus,
            Long productId,
            String productName,
            String productPreview,
            Long originalPrice,
            Long promotionPrice,
            Long optionId,
            String optionName,
            Long extraPrice,
            Long decreaseStockAmount
        ) {

        }
    }

}
