package on.ssgdeal.order_service.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.domain.vo.TotalOrderNumber;
import on.ssgdeal.order_service.domain.vo.TotalPrice;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class TotalOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private TotalOrderNumber totalOrderNumber;

    @Embedded
    private TotalPrice price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TotalOrderStatus status;

    @OneToOne(mappedBy = "totalOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Orderer orderer;

    @OneToMany(mappedBy = "totalOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TotalOrderPayment> totalOrderPayments;

    public void addDependencies(Orderer orderer, List<TotalOrderPayment> totalOrderPayments) {
        addOrdererDependency(orderer);
        addTotalOrderPaymentsDependency(totalOrderPayments);
    }

    private void addOrdererDependency(Orderer orderer) {
        this.orderer = orderer;
    }

    private void addTotalOrderPaymentsDependency(List<TotalOrderPayment> totalOrderPayments) {
        this.totalOrderPayments = totalOrderPayments;
    }
}
