package on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos;

public record CancelOrderPaymentRequestDto(Long cancelAmount) {

    public static CancelOrderPaymentRequestDto from(Long cancelAmount) {
        return new CancelOrderPaymentRequestDto(cancelAmount);
    }
}
