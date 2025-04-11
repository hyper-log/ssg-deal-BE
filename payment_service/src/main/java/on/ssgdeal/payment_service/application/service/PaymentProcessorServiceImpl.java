package on.ssgdeal.payment_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentConfirmException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentNotFoundException;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.PaymentClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentProcessorServiceImpl implements PaymentProcessorService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final PaymentClient paymentClient;

    @Override
    @Transactional
    public OrderPaymentResponseDto orderPayment(OrderPaymentRequestDto requestDto) {
        // TODO: total order id 검증
        Payment savedPayment = paymentService.savePayment(requestDto);
        Payment managedPayment = paymentRepository.findById(savedPayment.getId())
            .orElseThrow(PaymentNotFoundException::new);
        try {
            final var responseDto = paymentClient.confirmPayment(
                requestDto.toPaymentRequestDto(generateOrderId(requestDto.totalOrderId())));
            managedPayment.complete();
            return OrderPaymentResponseDto.success(responseDto, managedPayment);
        } catch (PaymentConfirmException e) {
            managedPayment.fail(e.getFailReason());
            return OrderPaymentResponseDto.fail(managedPayment);
        }
    }

    private String generateOrderId(Long internalOrderId) {
        return "ORD-" + String.format("%06d", internalOrderId);
    }
}
