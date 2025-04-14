package on.ssgdeal.cart_service.infrastructure.client.product.feign.dto;

public record GetProductDetailsResponse(
    String promotionStatus,
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
