package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import org.springframework.data.domain.Pageable;

@Builder
public record GetPromotionsRequestDto(
        String keyword,
        String filter,
        Pageable pageable
) {
    public static GetPromotionsConditionDto toDto(GetPromotionsRequestDto requestDto) {
        return GetPromotionsConditionDto.builder()
                .keyword(requestDto.keyword())
                .filter(PromotionStatus.from(requestDto.filter()))
                .pageable(requestDto.pageable())
                .build();
    }
}
