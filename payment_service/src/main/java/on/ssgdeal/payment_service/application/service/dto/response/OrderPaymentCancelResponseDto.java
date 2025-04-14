package on.ssgdeal.payment_service.application.service.dto.response;

import lombok.Builder;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentResult;

@Builder
public record OrderPaymentCancelResponseDto(
    String paymentKey,
    String totalOrderId,
    Long totalAmount,
    String result
) {

    public static OrderPaymentCancelResponseDto success(Payment payment) {
        return OrderPaymentCancelResponseDto.builder()
            .paymentKey(payment.getPaymentKey())
            .totalOrderId(String.valueOf(payment.getTotalOrderId()))
            .totalAmount(payment.getPaymentAmount())
            .result(String.valueOf(PaymentResult.SUCCESS))
            .build();
    }

    public static OrderPaymentCancelResponseDto fail(Payment payment) {
        return OrderPaymentCancelResponseDto.builder()
            .paymentKey(payment.getPaymentKey())
            .totalOrderId(String.valueOf(payment.getTotalOrderId()))
            .totalAmount(payment.getPaymentAmount())
            .result(String.valueOf(PaymentResult.FAIL))
            .build();
    }
}
