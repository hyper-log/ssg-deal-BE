package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelOrderPaymentResponseDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos.CancelTotalOrderPaymentResponseDto;

public interface PaymentService {

    CancelTotalOrderPaymentResponseDto cancelTotalOrderPayment(Long totalOrderId,
        CancelTotalOrderPaymentRequestDto requestDto);

    CancelOrderPaymentResponseDto cancelOrderPayment(Long totalOrderId,
        CancelOrderPaymentRequestDto requestDto);

}