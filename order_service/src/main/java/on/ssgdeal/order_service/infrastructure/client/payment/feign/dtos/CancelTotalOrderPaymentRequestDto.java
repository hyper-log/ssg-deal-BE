package on.ssgdeal.order_service.infrastructure.client.payment.feign.dtos;

public record CancelTotalOrderPaymentRequestDto(
    String cancelReason,
    Long cancelAmount
) {

    public static CancelTotalOrderPaymentRequestDto from(
        String cancelReason,
        Long cancelAmount
    ) {
        return new CancelTotalOrderPaymentRequestDto(cancelReason, cancelAmount);
    }

}
