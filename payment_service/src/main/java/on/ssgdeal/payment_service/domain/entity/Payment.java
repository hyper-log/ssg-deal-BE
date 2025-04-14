package on.ssgdeal.payment_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.payment_service.application.service.dto.request.OrderPaymentRequestDto;
import on.ssgdeal.payment_service.domain.enums.PaymentFailReason;
import on.ssgdeal.payment_service.domain.enums.PaymentMethod;
import on.ssgdeal.payment_service.domain.enums.PaymentStatus;
import on.ssgdeal.payment_service.domain.enums.PaymentType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long totalOrderId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private Long paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentFailReason paymentFailReason;

    @Column(nullable = false)
    private Long balanceAmount;

    public static Payment create(OrderPaymentRequestDto dto) {
        return Payment.builder()
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

    public void complete() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void fail(PaymentFailReason paymentFailReason) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.paymentFailReason = paymentFailReason;
    }

    public void cancel() {
        this.paymentStatus = PaymentStatus.CANCELED;
    }
}
