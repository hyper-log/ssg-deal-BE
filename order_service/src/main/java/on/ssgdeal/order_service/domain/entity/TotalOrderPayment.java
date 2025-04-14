package on.ssgdeal.order_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateCancelOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentStatus;
import on.ssgdeal.order_service.domain.enums.PaymentType;
import org.hibernate.annotations.SQLRestriction;


@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class TotalOrderPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_order_id")
    private TotalOrder totalOrder;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Long paymentAmount;
    private String paymentKey;
    private Timestamp paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentFailReason;

    public static TotalOrderPayment create(TotalOrder totalOrder) {
        return TotalOrderPayment.builder()
            .totalOrder(totalOrder)
            .paymentStatus(PaymentStatus.PENDING)
            .build();
    }

    public static TotalOrderPayment updateCancelOrder(TotalOrder totalOrder,
        UpdateCancelOrderSuccessDto dto) {
        return TotalOrderPayment.builder()
            .totalOrder(totalOrder)
            .paymentId(dto.paymentId())
            .paymentType(dto.paymentType())
            .paymentMethod(dto.paymentMethod())
            .paymentAmount(dto.paymentAmount())
            .paymentKey(dto.paymentKey())
            .paymentDate(Timestamp.valueOf(dto.paymentDate()))
            .paymentStatus(PaymentStatus.COMPLETED)
            .build();
    }
}