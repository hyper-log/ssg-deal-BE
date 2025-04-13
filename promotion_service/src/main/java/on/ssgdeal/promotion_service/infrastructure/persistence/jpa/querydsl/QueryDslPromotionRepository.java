package on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QueryDslPromotionRepository {
    Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long promotionId, Pageable pageable);
}
