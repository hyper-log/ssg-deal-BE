package on.ssgdeal.promotion_service.infrastructure.persistence.jpa;

import on.ssgdeal.promotion_service.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Long> {
}
