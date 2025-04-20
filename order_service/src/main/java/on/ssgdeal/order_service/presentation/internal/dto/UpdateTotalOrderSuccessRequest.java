package on.ssgdeal.order_service.presentation.internal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentType;

public record UpdateTotalOrderSuccessRequest(
    @NotNull(message = "totalOrderId는 필수 입력 값입니다.") Long totalOrderId,
    @NotNull(message = "paymentId는 필수 입력 값입니다.") Long paymentId,
    @NotNull(message = "paymentType은 필수 입력 값입니다.") PaymentType paymentType,
    @NotNull(message = "paymentMethod는 필수 입력 값입니다.") PaymentMethod paymentMethod,
    @NotNull(message = "paymentAmount는 필수 입력 값입니다.") Long paymentAmount,
    @NotNull(message = "paymentDate는 필수 입력 값입니다.") LocalDateTime paymentDate,
    @NotBlank(message = "paymentKey는 필수 입력 값입니다.") String paymentKey) {

    public UpdateTotalOrderSuccessRequestDto toDto() {
        return new UpdateTotalOrderSuccessRequestDto(
            totalOrderId,
            paymentId,
            paymentType,
            paymentMethod,
            paymentAmount,
            paymentDate,
            paymentKey);
    }

}
