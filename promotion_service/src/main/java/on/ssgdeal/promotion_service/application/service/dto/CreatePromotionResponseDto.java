package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.Promotion;

@Builder
public record CreatePromotionResponseDto(
        Long promotionId
) {
    public static CreatePromotionResponseDto from(Promotion promotion) {
        return CreatePromotionResponseDto.builder()
                .promotionId(promotion.getId())
                .build();
    }
}
