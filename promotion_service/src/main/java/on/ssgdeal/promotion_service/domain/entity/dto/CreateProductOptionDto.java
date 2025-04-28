package on.ssgdeal.promotion_service.domain.entity.dto;

import lombok.Builder;


@Builder
public record CreateProductOptionDto(
        String optionName,
        Long extraPrice,
        Long productStock
) {
}