package on.ssgdeal.payment_service.application.service.dto.response;

public record GetPaymentsResponseDto(
    String paymentKey,
    String totalOrderId,
    Long totalAmount,
    String paymentStatus
) {

}
