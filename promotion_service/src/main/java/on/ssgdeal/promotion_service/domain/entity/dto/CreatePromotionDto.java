package on.ssgdeal.promotion_service.domain.entity.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;

import java.time.LocalDate;

@Builder
public record CreatePromotionDto(
        String title,
        String previewUrl,
        String contentImageUrl,
        String content,
        PromotionStatus status,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate,
        CreateCompanyDto companyDto
) {
}