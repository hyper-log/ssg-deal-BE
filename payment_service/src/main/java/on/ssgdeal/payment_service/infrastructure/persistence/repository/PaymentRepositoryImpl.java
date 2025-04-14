package on.ssgdeal.payment_service.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import on.ssgdeal.payment_service.infrastructure.persistence.jpa.PaymentJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentJpaRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByTotalOrderIdAndPaymentStatus(
        Long totalOrderId,
        PaymentStatus paymentStatus) {
        return paymentJpaRepository.findByTotalOrderIdAndPaymentStatus(totalOrderId, paymentStatus);
    }

    @Override
    public Page<Payment> findByPaymentStatus(Long totalOrderId, PaymentStatus status,
        Pageable pageable) {
        return paymentJpaRepository
            .findByTotalOrderIdAndPaymentStatus(totalOrderId, PaymentStatus.COMPLETED, pageable);
    }
}
