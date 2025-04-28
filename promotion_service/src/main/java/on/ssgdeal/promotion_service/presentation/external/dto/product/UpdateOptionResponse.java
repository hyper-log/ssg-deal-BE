package on.ssgdeal.promotion_service.presentation.external.dto.product;

public record UpdateOptionResponse(
    Long optionId,
    String optionName,
    Long extraPrice,
    Long productStock
) {

}
