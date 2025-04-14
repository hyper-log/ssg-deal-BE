package on.ssgdeal.order_service.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderDto;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.vo.Price;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@Table(name = "Orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "total_order_id")
    private TotalOrder totalOrder;

    private Price price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Long companyId;
    private String companyName;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @BatchSize(size = 100)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public static List<Order> create(TotalOrder totalOrder, List<CreateOrderDto> createOrderDto) {
        return createOrderDto.stream().map(dto -> Order.builder()
                .totalOrder(totalOrder)
                .price(new Price(dto.orderTotalPrice()))
                .status(OrderStatus.PENDING)
                .companyId(dto.companyId())
                .companyName(dto.companyName())
                .build())
            .toList();
    }

    public void addOrderProductDependency(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void cancel() {
        if (status != OrderStatus.CANCELED) {
            status = OrderStatus.CANCELED;
        }
    }
}
