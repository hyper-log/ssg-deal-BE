package on.ssgdeal.promotion_service.infrastructure.persistence.jpa;

import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOption, Long> {

}
