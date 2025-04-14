package on.ssgdeal.order_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.order_service.exception.OrderException.OrderNullPriceException;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalPrice {

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private Long value;

    public TotalPrice(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value == null) {
            throw new OrderNullPriceException();
        }
    }

    public TotalPrice updateCancelTotalPrice(final Long orderPrice) {

        return new TotalPrice(this.value - orderPrice);
    }
}
