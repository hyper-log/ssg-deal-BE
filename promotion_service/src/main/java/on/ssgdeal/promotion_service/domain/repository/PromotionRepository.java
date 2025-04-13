package on.ssgdeal.promotion_service.domain.repository;

import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromotionRepository {

    Optional<Promotion> findById(Long id);
    Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long id, Pageable pageable);

}
