package on.ssgdeal.promotion_service.presentation.external.dto.product;

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

}
