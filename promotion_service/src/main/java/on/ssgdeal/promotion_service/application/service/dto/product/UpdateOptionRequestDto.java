package on.ssgdeal.promotion_service.application.service.dto.product;

import lombok.Builder;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateOptionRequest;

@Builder
public record UpdateOptionRequestDto(
    Long optionId,
    String optionName,
    Long extraPrice,
    Long productStock
) {

    public static UpdateOptionRequestDto from(
        Long optionId,
        UpdateOptionRequest request
    ) {
        return UpdateOptionRequestDto.builder()
            .optionId(optionId)
            .optionName(request.optionName())
            .extraPrice(request.extraPrice())
            .productStock(request.productStock())
            .build();
    }
}
