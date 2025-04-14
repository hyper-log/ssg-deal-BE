package on.ssgdeal.order_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderProductDto;
import on.ssgdeal.order_service.domain.vo.Quantity;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Embedded
    private Quantity quantity;

    private String previewUrl;

    @Column(nullable = false)
    private Long originalPrice;

    @Column(nullable = false)
    private Long promotionPrice;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private Long extraPrice;

    @Column(nullable = false)
    private Long totalPrice;

    public static OrderProduct create(Order order, CreateOrderProductDto dto) {
        return OrderProduct.builder()
            .order(order)
            .productId(dto.productId())
            .productName(dto.productName())
            .quantity(new Quantity(dto.quantity()))
            .previewUrl(dto.previewUrl())
            .originalPrice(dto.originalPrice())
            .promotionPrice(dto.promotionPrice())
            .optionId(dto.optionId())
            .optionName(dto.optionName())
            .extraPrice(dto.extraPrice())
            .totalPrice(dto.totalPrice())
            .build();
    }
}
