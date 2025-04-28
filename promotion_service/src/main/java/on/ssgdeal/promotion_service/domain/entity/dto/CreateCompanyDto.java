package on.ssgdeal.promotion_service.domain.entity.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;

import java.time.LocalDate;

@Builder
public record CreateCompanyDto(
        Long managerId,
        String companyName,
        String logoUrl
) {
}