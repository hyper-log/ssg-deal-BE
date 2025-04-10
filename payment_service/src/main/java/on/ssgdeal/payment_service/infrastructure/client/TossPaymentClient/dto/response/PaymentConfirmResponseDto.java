package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.response;

public record PaymentConfirmResponseDto(
    String paymentKey,
    String orderId,
    int totalAmount
) {

}
