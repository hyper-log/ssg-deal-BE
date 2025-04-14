package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.Promotion;

import java.time.LocalDate;

@Builder
public record GetPromotionsResponseDto(
        Long promotionId,
        String promotionTitle,
        String promotionPreviewUrl,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate
) {
    public static GetPromotionsResponseDto from(Promotion promotion) {
        return GetPromotionsResponseDto.builder()
                .promotionId(promotion.getId())
                .promotionTitle(promotion.getTitle())
                .promotionPreviewUrl(promotion.getPreviewUrl().getValue())
                .startPromotionDate(promotion.getStartPromotionDate())
                .endPromotionDate(promotion.getEndPromotionDate())
                .build();
    }
}
