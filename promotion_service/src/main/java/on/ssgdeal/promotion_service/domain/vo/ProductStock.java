package on.ssgdeal.promotion_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.promotion_service.exception.ProductException;


@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class ProductStock {

    private static final int MIN_SIZE = 0;

    @Column(name = "product_stock", nullable = false)
    private Long value;

    public ProductStock(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(Long value) {
        if (value < MIN_SIZE) {
            throw new IllegalArgumentException("상품 재고는 " + MIN_SIZE + " 이상이어야 합니다.");
        }
    }

    public void decrease(Long decreaseAmount) {
        if (value - decreaseAmount < MIN_SIZE) {
            throw new ProductException.ProductDoNotExistException();
        }
        
        value -= decreaseAmount;
    }

    public void increase(Long increaseAmount) {
        value += increaseAmount;
    }

}
