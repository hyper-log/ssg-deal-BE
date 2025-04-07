package on.ssgdeal.order_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.order_service.exception.OrderException;
import on.ssgdeal.order_service.exception.OrderExceptionCode;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalPrice {

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    private BigDecimal value;

    public TotalPrice(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value == null) {
            throw new OrderException(OrderExceptionCode.ORDER_NULL_PRICE);
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderException(OrderExceptionCode.ORDER_MIN_PRICE);
        }
    }
}
