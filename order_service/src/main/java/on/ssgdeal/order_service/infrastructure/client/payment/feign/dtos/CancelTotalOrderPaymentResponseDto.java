package on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos;

public record CancelTotalOrderPaymentResponseDto(String paymentKey,
                                                 Long totalOrderId,
                                                 Long totalAmount,
                                                 String result
) {

}
