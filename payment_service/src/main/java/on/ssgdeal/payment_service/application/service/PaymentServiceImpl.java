package on.ssgdeal.payment_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.GetPaymentsResponseDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Payment getPaymentByTotalOrderId(final Long totalOrderId) {
        return paymentRepository.findByTotalOrderIdAndPaymentStatus(totalOrderId,
                PaymentStatus.COMPLETED)
            .orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public PageDto<GetPaymentsResponseDto> getPayments(final Long totalOrderId, Pageable pageable) {
        Page<Payment> paymentsPage = paymentRepository
            .findByPaymentStatus(totalOrderId, PaymentStatus.COMPLETED, pageable);

        Page<GetPaymentsResponseDto> dtoPage = paymentsPage.map(payment ->
            new GetPaymentsResponseDto(
                payment.getPaymentKey(),
                payment.getTotalOrderId().toString(),
                payment.getPaymentAmount(),
                payment.getPaymentStatus().name()
            )
        );

        return PageDto.from(dtoPage);
    }
}
