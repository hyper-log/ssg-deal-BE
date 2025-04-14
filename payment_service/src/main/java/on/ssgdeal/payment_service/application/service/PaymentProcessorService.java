package on.ssgdeal.payment_service.application.service;

import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentPartialCancelRequestDto;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentPartialCancelResponseDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;

public interface PaymentProcessorService {

    OrderPaymentResponseDto orderPayment(OrderPaymentRequestDto requestDto);

    OrderPaymentCancelResponseDto orderPaymentCancel(Long totalOrderId);

    OrderPaymentPartialCancelResponseDto orderPaymentPartialCancel(Long totalOrderId,
        OrderPaymentPartialCancelRequestDto requestDto);
}
