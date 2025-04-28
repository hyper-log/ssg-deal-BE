package on.ssgdeal.payment_service.infrastructure.client.PaymentClient.dto.response;

public record PaymentConfirmResponseDto(
    String paymentKey,
    String orderId,
    int totalAmount
) {

}
