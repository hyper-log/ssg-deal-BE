package on.ssgdeal.payment_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentPartialCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import on.ssgdeal.payment_service.exception.PaymentException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentCancelException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentConfirmException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentNotFoundException;
import on.ssgdeal.payment_service.exception.PaymentExceptionCode;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.PaymentClient;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentFailRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentSuccessRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentProcessorServiceImpl implements PaymentProcessorService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final PaymentClient paymentClient;
    private final OrderService orderService;

    @Override
    @Transactional
    public OrderPaymentResponseDto orderPayment(OrderPaymentRequestDto requestDto) {
        validTotalOrderId(requestDto.totalOrderId());

        Payment savedPayment = paymentService.savePayment(requestDto);
        Payment managedPayment = paymentRepository.findById(savedPayment.getId())
            .orElseThrow(PaymentNotFoundException::new);

        try {
            final var responseDto = paymentClient.confirmPayment(
                requestDto.toPaymentRequestDto(generateOrderId(requestDto.totalOrderId())));
            managedPayment.complete();
            final var successRequestDto = CreateOrderPaymentSuccessRequestDto.from(
                managedPayment);
            orderService.createOrderPaymentSuccess(successRequestDto);
            return OrderPaymentResponseDto.success(responseDto, managedPayment);
        } catch (PaymentConfirmException e) {
            managedPayment.fail(e.getFailReason());
            final var failRequestDto = CreateOrderPaymentFailRequestDto.from(
                managedPayment);
            orderService.createOrderPaymentFail(failRequestDto);
            return OrderPaymentResponseDto.fail(managedPayment);
        }
    }

    @Override
    @Transactional
    public OrderPaymentCancelResponseDto orderPaymentCancel(final Long totalOrderId) {
        validTotalOrderId(totalOrderId);

        Payment payment = paymentService.getPaymentByTotalOrderId(totalOrderId);

        try {
            paymentClient.cancelPayment(payment);
            payment.cancel();
            return OrderPaymentCancelResponseDto.success(payment);
        } catch (PaymentCancelException e) {
            return OrderPaymentCancelResponseDto.fail(payment);
        }
    }

    @Override
    @Transactional
    public OrderPaymentPartialCancelResponseDto orderPaymentPartialCancel(Long totalOrderId,
        OrderPaymentPartialCancelRequestDto requestDto) {
        validTotalOrderId(totalOrderId);

        Payment payment = paymentService.getPaymentByTotalOrderId(totalOrderId);

        try {
            paymentClient.partialCancelPayment(payment, requestDto);
            payment.cancel();
            return OrderPaymentPartialCancelResponseDto.success(payment);
        } catch (PaymentCancelException e) {
            return OrderPaymentPartialCancelResponseDto.fail(payment);
        }
    }

    private String generateOrderId(Long internalOrderId) {
        return "ORD-" + String.format("%06d", internalOrderId);
    }

    protected void validTotalOrderId(final Long totalOrderId) {
        if (!orderService.getValidTotalOrderId(totalOrderId).totalOrderExists()) {
            throw new PaymentException(PaymentExceptionCode.TOTAL_ORDER_ID_NOT_FOUND);
        }
    }
}