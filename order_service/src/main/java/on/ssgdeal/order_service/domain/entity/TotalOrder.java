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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.order_service.domain.entity.dtos.CreateTotalOrderDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateCancelOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.domain.vo.TotalOrderNumber;
import on.ssgdeal.order_service.domain.vo.TotalPrice;
import on.ssgdeal.order_service.exception.OrderException.OrderNotFoundOrderException;
import org.hibernate.annotations.BatchSize;
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

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "totalOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TotalOrderPayment> totalOrderPayments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "totalOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public static TotalOrder create(CreateTotalOrderDto createTotalOrderDto) {
        return TotalOrder.builder()
            .totalOrderNumber(new TotalOrderNumber(createTotalOrderDto.totalOrderNumber()))
            .status(TotalOrderStatus.PENDING)
            .price(new TotalPrice(createTotalOrderDto.price()))
            .build();
    }

    public void addDependencies(
        Orderer orderer,
        List<TotalOrderPayment> totalOrderPayments,
        List<Order> orders
    ) {
        addOrdererDependency(orderer);
        addTotalOrderPaymentsDependency(totalOrderPayments);
        addOrdersDependency(orders);
    }

    public void addDependencies(
        Orderer orderer,
        TotalOrderPayment totalOrderPayments,
        List<Order> orders
    ) {
        addOrdererDependency(orderer);
        addTotalOrderPaymentsDependency(totalOrderPayments);
        addOrdersDependency(orders);
    }

    private void addOrdererDependency(Orderer orderer) {
        this.orderer = orderer;
    }

    private void addOrdersDependency(List<Order> orders) {
        this.orders = orders;
    }

    private void addTotalOrderPaymentsDependency(List<TotalOrderPayment> totalOrderPayments) {
        this.totalOrderPayments = totalOrderPayments;
    }

    private void addTotalOrderPaymentsDependency(TotalOrderPayment totalOrderPayment) {
        this.totalOrderPayments.add(totalOrderPayment);
    }

    public void updateCancelTotalPrice(Long orderPrice) {
        this.price = price.updateCancelTotalPrice(orderPrice);
    }

    public void addCancelPayment(UpdateCancelOrderSuccessDto dto) {
        TotalOrderPayment payment = TotalOrderPayment.updateCancelOrder(this, dto);
        this.totalOrderPayments.add(payment);
    }

    public void updateCancelStatus() {
        this.status = TotalOrderStatus.CANCELED;
    }

    public void cancelSpecificOrder(Long orderId) {
        Order order = this.orders.stream()
            .filter(o -> o.getId().equals(orderId))
            .findFirst()
            .orElseThrow(OrderNotFoundOrderException::new);

        order.cancel();
    }

    public Boolean cancelAlreadyOrder(Long orderId) {
        Order order = this.orders.stream()
            .filter(o -> o.getId().equals(orderId))
            .findFirst()
            .orElseThrow(OrderNotFoundOrderException::new);
        return order.getStatus().equals(OrderStatus.CANCELED);
    }
}
