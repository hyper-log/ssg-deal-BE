package on.ssgdeal.promotion_service.application.service.dto.product;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder(access = AccessLevel.PRIVATE)
public record FindProductByPromotionIdRequestDto(
    Long promotionId,
    Pageable pageable
) {

    public static FindProductByPromotionIdRequestDto from(
        Long promotionId,
        Pageable pageable
    ) {
        return FindProductByPromotionIdRequestDto.builder()
            .promotionId(promotionId)
            .pageable(pageable)
            .build();
    }
}
