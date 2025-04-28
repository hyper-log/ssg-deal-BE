package on.ssgdeal.promotion_service.presentation.external.dto.product;

public record UpdateProductRequest(
    String productName,
    Long originalPrice,
    Long promotionPrice,
    String previewUrl,
    String contentImgUrl,
    String content
) {

}
