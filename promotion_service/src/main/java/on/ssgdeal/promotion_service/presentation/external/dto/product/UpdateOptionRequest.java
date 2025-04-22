package on.ssgdeal.promotion_service.presentation.external.dto.product;

public record UpdateOptionRequest(
    String optionName,
    Long extraPrice,
    Long productStock
) {

}
