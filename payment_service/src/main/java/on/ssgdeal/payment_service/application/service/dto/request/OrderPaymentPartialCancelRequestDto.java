package on.ssgdeal.payment_service.application.service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderPaymentPartialCancelRequestDto(

    @NotNull(message = "결제 취소 금액은 필수 입력값입니다.")
    @Positive(message = "결제 취소 금액은 양수만 가능합니다.")
    Long cancelAmount
) {

}
