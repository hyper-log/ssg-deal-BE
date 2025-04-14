package on.ssgdeal.order_service.infrastructure.persistence.jpa;

import on.ssgdeal.order_service.domain.entity.TotalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalOrderJpaRepository extends JpaRepository<TotalOrder, Long> {

}
