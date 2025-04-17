package on.ssgdeal.promotion_service.presentation.internal.dto.product;

public record GetProductStockRequest(
    Long productId,
    Long optionId
) {

    public static GetProductStockRequest from(Long productId, Long optionId) {
        return new GetProductStockRequest(productId, optionId);
    }

}
