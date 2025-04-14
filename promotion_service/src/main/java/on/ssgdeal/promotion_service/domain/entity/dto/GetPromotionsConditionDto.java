package on.ssgdeal.promotion_service.domain.entity.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import org.springframework.data.domain.Pageable;


@Builder
public record GetPromotionsConditionDto(
        String keyword,
        PromotionStatus filter,
        Pageable pageable
) {
}