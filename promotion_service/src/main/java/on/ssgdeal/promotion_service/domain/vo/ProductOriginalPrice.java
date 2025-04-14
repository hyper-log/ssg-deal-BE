package on.ssgdeal.promotion_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.promotion_service.domain.vo.validator.PriceValidator;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class ProductOriginalPrice {

    private static final int MIN_SIZE = 0;
    private static final String ERROR_MESSAGE_FIELD = "상품 원가";

    @Column(name = "original_price", nullable = false)
    private Long value;

    public ProductOriginalPrice(final Long value) {
        PriceValidator.validate(value, MIN_SIZE, ERROR_MESSAGE_FIELD);
        this.value = value;
    }

}
