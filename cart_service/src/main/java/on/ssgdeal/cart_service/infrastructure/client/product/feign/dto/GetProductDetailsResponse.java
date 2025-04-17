package on.ssgdeal.cart_service.infrastructure.client.product.feign.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record GetProductDetailsResponse(
    List<ProductDetail> productDetails
) {

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

        @Getter
        @AllArgsConstructor
        public enum PromotionStatus {

            PENDING("예정"),
            IN_PROGRESS("진행 중"),
            FINISHED("종료"),
            ;
            private final String description;
        }
    }

}
