package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentCancelRequestDto(
    @NotBlank(message = "결제 취소 사유는 필수 입력값입니다.")
    String cancelReason
) {

}
