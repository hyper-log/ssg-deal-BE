package on.ssgdeal.payment_service.domain.repository;

import java.util.Optional;
import on.ssgdeal.payment_service.domain.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);
}
