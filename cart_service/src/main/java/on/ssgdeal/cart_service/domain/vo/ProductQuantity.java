package on.ssgdeal.cart_service.domain.vo;

import lombok.Getter;
import on.ssgdeal.cart_service.exception.CartException.MustBePositiveQuantityException;

@Getter
public class ProductQuantity {

    private static final Long MIN_QUANTITY = 1L;

    private Long value;

    public ProductQuantity(Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(Long quantity) {
        if (quantity == null || quantity < MIN_QUANTITY) {
            throw new MustBePositiveQuantityException();
        }
    }

    public void increase(Long quantity) {
        validate(quantity);
        this.value += quantity;
    }
}
