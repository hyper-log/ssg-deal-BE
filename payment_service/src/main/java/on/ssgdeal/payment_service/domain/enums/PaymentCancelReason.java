package on.ssgdeal.payment_service.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentCancelReason {

    SIMPLE_CHANGE_OF_MIND("단순 변심"),
    ;

    final String description;

    PaymentCancelReason(String description) {
        this.description = description;
    }
}
