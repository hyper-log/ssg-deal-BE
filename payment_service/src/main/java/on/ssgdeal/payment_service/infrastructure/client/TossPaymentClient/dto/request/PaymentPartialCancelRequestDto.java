package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentPartialCancelRequestDto(
    @NotBlank(message = "결제 취소 사유는 필수 입력값입니다.")
    String cancelReason,

    @NotNull(message = "결제 취소 금액은 필수 입력값입니다.")
    @Positive(message = "결제 취소 금액은 양수만 가능합니다.")
    Long cancelAmount
) {

}
