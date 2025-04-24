package on.ssgdeal.payment_service.application.service.dto.response;

import lombok.Builder;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response.PaymentConfirmResponseDto;

@Builder
public record OrderPaymentResponseDto(
    String paymentKey,
    String totalOrderId,
    Long totalAmount,
    String paymentStatus,
    String paymentFailReason
) {

    public static OrderPaymentResponseDto success(
        PaymentConfirmResponseDto responseDto,
        Payment payment
    ) {
        return OrderPaymentResponseDto.builder()
            .paymentKey(responseDto.paymentKey())
            .totalOrderId(responseDto.orderId())
            .totalAmount((long) responseDto.totalAmount())
            .paymentStatus(String.valueOf(payment.getPaymentStatus()))
            .build();
    }

    public static OrderPaymentResponseDto fail(Payment payment) {
        return OrderPaymentResponseDto.builder()
            .paymentKey(payment.getPaymentKey())
            .totalOrderId(String.valueOf(payment.getTotalOrderId()))
            .totalAmount(payment.getPaymentAmount())
            .paymentStatus(String.valueOf(payment.getPaymentStatus()))
            .paymentFailReason(String.valueOf(payment.getPaymentFailReason()))
            .build();
    }
}
