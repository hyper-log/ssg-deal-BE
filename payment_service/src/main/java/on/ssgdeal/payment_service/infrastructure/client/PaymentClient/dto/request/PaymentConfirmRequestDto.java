package on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.request;

import lombok.Builder;

@Builder
public record PaymentConfirmRequestDto(
    String paymentKey,
    String orderId,
    Long amount
) {

}
