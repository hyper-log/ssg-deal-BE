package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.Promotion;

import java.time.LocalDate;

@Builder
public record GetFinishedPromotionDetailResponseDto(
        Long promotionId,
        String promotionTitle,
        String promotionContentImg,
        String promotionContent,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate
) {
    public static GetFinishedPromotionDetailResponseDto from(Promotion promotion) {
        return GetFinishedPromotionDetailResponseDto.builder()
                .promotionId(promotion.getId())
                .promotionTitle(promotion.getTitle())
                .promotionContentImg(promotion.getContentImageUrl().toString())
                .promotionContent(promotion.getContent())
                .startPromotionDate(promotion.getStartPromotionDate())
                .endPromotionDate(promotion.getEndPromotionDate())
                .build();
    }
}
