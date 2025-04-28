package on.ssgdeal.promotion_service.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.jpa.BaseEntity;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateProductDto;
import on.ssgdeal.promotion_service.domain.entity.dto.CreateProductOptionDto;
import on.ssgdeal.promotion_service.domain.vo.*;

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

    @Embedded
    private OptionName optionName;

    @Embedded
    private ProductOptionExtraPrice extraPrice;

    @Embedded
    private ProductStock productStock;

    public static ProductOption create(CreateProductOptionDto dto) {
        return ProductOption.builder()
                .optionName(new OptionName(dto.optionName()))
                .extraPrice(new ProductOptionExtraPrice(dto.extraPrice()))
                .productStock(new ProductStock(dto.productStock()))
                .build();
    }
}
