package on.ssgdeal.promotion_service.domain.entity;
import jakarta.persistence.*;
import lombok.*;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.vo.OptionName;
import on.ssgdeal.promotion_service.domain.vo.ProductOptionExtraPrice;
import on.ssgdeal.promotion_service.domain.vo.ProductStock;

@Entity
@Table(name = "product_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private OptionName optionName;

    @Embedded
    private ProductOptionExtraPrice extraPrice;

    @Embedded
    private ProductStock productStock;

}
