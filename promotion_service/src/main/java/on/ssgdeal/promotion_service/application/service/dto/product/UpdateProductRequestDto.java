package on.ssgdeal.promotion_service.application.service.dto.product;

import lombok.Builder;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateProductRequest;

@Builder
public record UpdateProductRequestDto(
    Long productId,
    String productName,
    Long originalPrice,
    Long promotionPrice,
    String previewUrl,
    String contentImgUrl,
    String content
) {

    public static UpdateProductRequestDto from(
        Long productId,
        UpdateProductRequest request
    ) {
        return UpdateProductRequestDto.builder()
            .productId(productId)
            .productName(request.productName())
            .originalPrice(request.originalPrice())
            .promotionPrice(request.promotionPrice())
            .previewUrl(request.previewUrl())
            .contentImgUrl(request.contentImgUrl())
            .content(request.content())
            .build();
    }

}
