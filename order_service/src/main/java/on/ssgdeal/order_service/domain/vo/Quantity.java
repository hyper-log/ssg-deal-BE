package on.ssgdeal.order_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.order_service.exception.OrderException.OrderMinQuantityException;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private Long value;

    public Quantity(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value == null || value <= 0) {
            throw new OrderMinQuantityException();
        }
    }
}
