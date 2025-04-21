package on.ssgdeal.payment_service.infrastructure.client.PaymentClient.strategy;

import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.request.PaymentConfirmRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response.PaymentCancelResponseDto;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response.PaymentConfirmResponseDto;

public interface PaymentStrategy {

    PaymentConfirmResponseDto confirm(PaymentConfirmRequestDto requestDto);

    PaymentCancelResponseDto cancel(Payment payment);

    PaymentCancelResponseDto partialCancel(Payment payment,
        OrderPaymentPartialCancelRequestDto partialRequestDto);
}
