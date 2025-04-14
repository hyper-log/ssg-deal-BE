package on.ssgdeal.promotion_service.presentation.external.dto.product;

public record FindByPromotionIdResponse(
    Long productId,
    String productName,
    String companyName,
    String productPreviewImgUrl,
    Long originalPrice,
    Long promotionPrice
) {

}
