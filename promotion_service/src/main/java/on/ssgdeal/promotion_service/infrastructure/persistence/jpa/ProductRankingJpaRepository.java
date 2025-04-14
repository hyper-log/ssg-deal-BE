package on.ssgdeal.promotion_service.infrastructure.persistence.jpa;

import on.ssgdeal.promotion_service.domain.entity.ProductRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRankingJpaRepository extends JpaRepository<ProductRanking, Long> {

}
