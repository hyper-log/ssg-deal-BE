package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.request;

import lombok.Builder;

@Builder
public record PaymentConfirmRequestDto(
    String paymentKey,
    String orderId,
    Long amount
) {

}
