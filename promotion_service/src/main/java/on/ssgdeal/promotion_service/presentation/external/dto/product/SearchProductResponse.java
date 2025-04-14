package on.ssgdeal.promotion_service.presentation.external.dto.product;

import on.ssgdeal.promotion_service.domain.entity.Product;

public record SearchProductResponse(
    Long productId,
    String productName,
    String companyName,
    String productPreviewImgUrl,
    Long originalPrice,
    Long promotionPrice
) {

    public static SearchProductResponse from(Product product) {
        return new SearchProductResponse(
            product.getId(),
            product.getName().getValue(),
            product.getCompany().getName().getValue(),
            product.getPreviewUrl().getValue(),
            product.getOriginalPrice().getValue(),
            product.getPromotionPrice().getValue()
        );
    }

}
