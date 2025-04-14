package on.ssgdeal.payment_service.domain.repository;

import java.util.Optional;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {

    Page<Payment> findByPaymentStatus(Long totalOrderId, PaymentStatus status, Pageable pageable);

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);

    Optional<Payment> findByTotalOrderIdAndPaymentStatus(
        Long totalOrderId,
        PaymentStatus paymentStatus
    );
}
