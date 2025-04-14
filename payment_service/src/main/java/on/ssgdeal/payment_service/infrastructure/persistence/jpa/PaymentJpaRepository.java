package on.ssgdeal.payment_service.infrastructure.persistence.jpa;

import java.util.Optional;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTotalOrderIdAndPaymentStatus(Long totalOrderId,
        PaymentStatus paymentStatus);

    Page<Payment> findByTotalOrderIdAndPaymentStatus(Long totalOrderId,
        PaymentStatus paymentStatus, Pageable pageable);
}
