package on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.TossPaymentClient;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.request.PaymentConfirmRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response.PaymentCancelResponseDto;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response.PaymentConfirmResponseDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossPaymentStrategy implements PaymentStrategy {

    private final TossPaymentClient paymentClient;

    @Override
    public PaymentConfirmResponseDto confirm(PaymentConfirmRequestDto requestDto) {
        return paymentClient.confirmPayment(requestDto);
    }

    @Override
    public PaymentCancelResponseDto cancel(Payment payment) {
        return paymentClient.cancelPayment(payment);
    }

    @Override
    public PaymentCancelResponseDto partialCancel(Payment payment,
        OrderPaymentPartialCancelRequestDto partialRequestDto) {
        return paymentClient.partialCancelPayment(payment, partialRequestDto);
    }
}

