package on.ssgdeal.promotion_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.JpaPromotionRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl.QueryDslPromotionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {

    private final JpaPromotionRepository jpaPromotionRepository;
    private final QueryDslPromotionRepository queryDslPromotionRepository;

    @Override
    public Optional<Promotion> findById(Long id) {
        return jpaPromotionRepository.findById(id);
    }

    @Override
    public Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long promotionId, Pageable pageable) {
        return queryDslPromotionRepository.findPromotionWithProductsById(promotionId, pageable);
    }
}
