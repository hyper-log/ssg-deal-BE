package on.ssgdeal.payment_service.infrastructure.client.order.feign.dto;

import lombok.Builder;
import on.ssgdeal.payment_service.domain.entity.Payment;

@Builder
public record CreateOrderPaymentFailRequestDto(
    Long totalOrderId
) {

    public static CreateOrderPaymentFailRequestDto from(Payment payment) {
        return CreateOrderPaymentFailRequestDto.builder()
            .totalOrderId(payment.getTotalOrderId())
            .build();
    }
}
