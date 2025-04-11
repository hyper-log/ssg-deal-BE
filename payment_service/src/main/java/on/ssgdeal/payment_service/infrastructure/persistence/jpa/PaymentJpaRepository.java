package on.ssgdeal.payment_service.infrastructure.persistence.jpa;

import on.ssgdeal.payment_service.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

}
