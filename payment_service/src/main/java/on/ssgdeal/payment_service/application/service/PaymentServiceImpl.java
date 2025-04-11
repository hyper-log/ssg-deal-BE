package on.ssgdeal.payment_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment savePayment(OrderPaymentRequestDto dto) {
        Payment payment = Payment.create(dto);
        return paymentRepository.save(payment);
    }

}
