package on.ssgdeal.payment_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentPartialCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentType;
import on.ssgdeal.payment_service.domain.repository.PaymentRepository;
import on.ssgdeal.payment_service.exception.PaymentException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentCancelException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentConfirmException;
import on.ssgdeal.payment_service.exception.PaymentException.PaymentNotFoundException;
import on.ssgdeal.payment_service.exception.PaymentExceptionCode;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy.PaymentStrategy;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy.PaymentStrategyFactory;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentFailRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentSuccessRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentProcessorServiceImpl implements PaymentProcessorService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PaymentStrategyFactory paymentStrategyFactory;

    @Override
    @Transactional
    public OrderPaymentResponseDto orderPayment(OrderPaymentRequestDto requestDto) {
        validTotalOrderId(requestDto.totalOrderId());

        Payment savedPayment = paymentService.savePayment(requestDto);
        Payment managedPayment = paymentRepository.findById(savedPayment.getId())
            .orElseThrow(PaymentNotFoundException::new);

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(
            PaymentType.valueOf(requestDto.paymentType()));

        try {
            var responseDto = strategy.confirm(
                requestDto.toPaymentRequestDto(generateOrderId(requestDto.totalOrderId())));
            managedPayment.complete();
            var successRequestDto = CreateOrderPaymentSuccessRequestDto.from(managedPayment);
            orderService.createOrderPaymentSuccess(successRequestDto);
            return OrderPaymentResponseDto.success(responseDto, managedPayment);
        } catch (PaymentConfirmException e) {
            managedPayment.fail(e.getFailReason());
            var failRequestDto = CreateOrderPaymentFailRequestDto.from(managedPayment);
            orderService.createOrderPaymentFail(failRequestDto);
            return OrderPaymentResponseDto.fail(managedPayment);
        }
    }

    @Override
    @Transactional
    public OrderPaymentCancelResponseDto orderPaymentCancel(Long totalOrderId) {
        validTotalOrderId(totalOrderId);

        Payment payment = paymentService.getPaymentByTotalOrderId(totalOrderId);
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(payment.getPaymentType());

        try {
            strategy.cancel(payment);
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
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(payment.getPaymentType());

        try {
            strategy.partialCancel(payment, requestDto);
            payment.cancel();
            return OrderPaymentPartialCancelResponseDto.success(payment);
        } catch (PaymentCancelException e) {
            return OrderPaymentPartialCancelResponseDto.fail(payment);
        }
    }

    private String generateOrderId(Long internalOrderId) {
        return "ORD-" + String.format("%06d", internalOrderId);
    }

    protected void validTotalOrderId(Long totalOrderId) {
        if (!orderService.getValidTotalOrderId(totalOrderId).totalOrderExists()) {
            throw new PaymentException(PaymentExceptionCode.TOTAL_ORDER_ID_NOT_FOUND);
        }
    }
}
