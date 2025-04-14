package on.ssgdeal.payment_service.application.fixture;

import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentMethod;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import on.ssgdeal.payment_service.domain.enums.PaymentType;
import on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.response.PaymentConfirmResponseDto;

public class FixtureFactory {

    public static OrderPaymentRequestDto getOrderPaymentRequestDto() {
        return OrderPaymentRequestDto.builder()
            .paymentKey("test-payment-key")
            .totalOrderId(1001L)
            .totalAmount(15000L)
            .paymentMethod(PaymentMethod.CARD.name())
            .paymentType(PaymentType.TOSS.name())
            .userId(42L)
            .build();
    }

    public static Payment getPayment() {
        return Payment.builder()
            .id(1L)
            .totalOrderId(1001L)
            .userId(42L)
            .paymentKey("test-payment-key")
            .paymentAmount(15000L)
            .paymentMethod(PaymentMethod.CARD)
            .paymentType(PaymentType.TOSS)
            .paymentStatus(PaymentStatus.PENDING)
            .balanceAmount(15000L)
            .build();
    }

    public static Payment getPayment(Long id) {
        return Payment.builder()
            .id(id)
            .totalOrderId(1001L)
            .userId(42L)
            .paymentKey("test-payment-key")
            .paymentAmount(15000L)
            .paymentMethod(PaymentMethod.CARD)
            .paymentType(PaymentType.TOSS)
            .paymentStatus(PaymentStatus.PENDING)
            .balanceAmount(15000L)
            .build();
    }

    public static Payment getPayment(OrderPaymentRequestDto dto, Long id) {
        return Payment.builder()
            .id(id)
            .totalOrderId(dto.totalOrderId())
            .userId(dto.userId())
            .paymentKey(dto.paymentKey())
            .paymentAmount(dto.totalAmount())
            .paymentMethod(PaymentMethod.valueOf(dto.paymentMethod()))
            .paymentType(PaymentType.valueOf(dto.paymentType()))
            .paymentStatus(PaymentStatus.PENDING)
            .balanceAmount(dto.totalAmount())
            .build();
    }

    public static PaymentConfirmResponseDto getPaymentConfirmResponseDto(
        OrderPaymentRequestDto dto) {
        return new PaymentConfirmResponseDto(
            dto.paymentKey(),
            "ORD-" + String.format("%06d", dto.totalOrderId()),
            dto.totalAmount().intValue()
        );
    }
}
