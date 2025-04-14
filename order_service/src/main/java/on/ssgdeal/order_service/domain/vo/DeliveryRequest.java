package on.ssgdeal.order_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import on.ssgdeal.order_service.exception.OrderException.OrderMaxDeliveryRequestException;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRequest {

    private static final int LENGTH = 20;

    @Column(name = "delivery_request", length = LENGTH)
    private String value;

    public DeliveryRequest(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value.length() > LENGTH) {
            throw new OrderMaxDeliveryRequestException();
        }
    }
}
