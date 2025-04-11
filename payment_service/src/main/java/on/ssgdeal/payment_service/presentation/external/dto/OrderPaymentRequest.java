package on.ssgdeal.payment_service.presentation.external.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;

public record OrderPaymentRequest(
    @NotBlank(message = "paymentKey 값이 공백일 수 없습니다.")
    String paymentKey,
    @NotNull(message = "orderId 값이 공백일 수 없습니다.")
    Long totalOrderId,
    @NotNull(message = "amount 값이 공백일 수 없습니다.")
    Long totalAmount,
    @NotBlank(message = "paymentMethod 값이 공백일 수 없습니다.")
    String paymentMethod,
    @NotBlank(message = "paymentType 값이 공백일 수 없습니다.")
    String paymentType
) {

    public OrderPaymentRequestDto toDto(Long userId) {
        return OrderPaymentRequestDto.builder()
            .paymentKey(paymentKey)
            .totalOrderId(totalOrderId)
            .totalAmount(totalAmount)
            .paymentMethod(paymentMethod)
            .paymentType(paymentType)
            .userId(userId)
            .build();
    }
}
