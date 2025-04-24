package on.ssgdeal.promotion_service.infrastructure.persistence.jpa;

import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findFirstByStatus(PromotionStatus status);
    Page<Promotion> findByStartPromotionDate(LocalDate startPromotionDate, Pageable pageable);
}
