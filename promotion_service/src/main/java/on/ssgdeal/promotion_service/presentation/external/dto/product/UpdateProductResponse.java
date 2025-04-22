package on.ssgdeal.promotion_service.presentation.external.dto.product;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.Product;

@Builder
public record UpdateProductResponse(
    Long productId,
    Long companyId,
    String productName,
    Long originalPrice,
    Long promotionPrice,
    String previewUrl,
    String contentImgUrl,
    String content
) {

    public static UpdateProductResponse from(Product product) {
        return UpdateProductResponse.builder()
            .productId(product.getId())
            .companyId(product.getCompany().getId())
            .productName(product.getName().getValue())
            .originalPrice(product.getOriginalPrice().getValue())
            .promotionPrice(product.getPromotionPrice().getValue())
            .previewUrl(product.getPreviewUrl().getValue())
            .contentImgUrl(product.getContentImgUrl().getValue())
            .content(product.getContent())
            .build();
    }

}
