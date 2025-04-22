package on.ssgdeal.promotion_service.infrastructure.persistence.jpa;

import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findFirstByStatus(PromotionStatus status);
}
