package on.ssgdeal.promotion_service.presentation.internal.dto.product;

import java.util.List;
import lombok.Builder;

@Builder
public record GetProductOptionsResponse(
    List<ProductOption> options
) {

    @Builder
    public record ProductOption(
        Long optionId,
        String optionName,
        Long extraPrice,
        Long productStock
    ) {

    }

}
