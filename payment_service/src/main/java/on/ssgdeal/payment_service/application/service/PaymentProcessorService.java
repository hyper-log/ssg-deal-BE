package on.ssgdeal.payment_service.application.service;

import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;

public interface PaymentProcessorService {

    OrderPaymentResponseDto orderPayment(OrderPaymentRequestDto requestDto);
}
