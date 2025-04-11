package on.ssgdeal.payment_service.application.service.dto.request;

import lombok.Builder;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.request.PaymentConfirmRequestDto;

@Builder
public record OrderPaymentRequestDto(
    String paymentKey,
    Long totalOrderId,
    Long totalAmount,
    String paymentMethod,
    String paymentType,
    Long userId
) {

    public PaymentConfirmRequestDto toPaymentRequestDto(String convertedOrderId) {
        return PaymentConfirmRequestDto.builder()
            .paymentKey(paymentKey)
            .orderId(convertedOrderId)
            .amount(totalAmount)
            .build();
    }
}
