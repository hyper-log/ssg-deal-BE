package on.ssgdeal.order_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import on.ssgdeal.order_service.exception.OrderException;
import on.ssgdeal.order_service.exception.OrderExceptionCode;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalOrderNumber {

    private static final int LENGTH = 10;

    @Column(name = "total_order_number", nullable = false, length = LENGTH)
    private String value;

    public TotalOrderNumber(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value)) {
            throw new OrderException(OrderExceptionCode.ORDER_NULL_TOTAL_ORDER_NUMBER);
        }

        if (!value.matches("^\\d{6}[0-9]{4}$")) {
            throw new OrderException(OrderExceptionCode.ORDER_FORMAT_TOTAL_ORDER_NUMBER);
        }
    }

}
