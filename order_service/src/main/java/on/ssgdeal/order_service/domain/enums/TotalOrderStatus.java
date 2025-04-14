package on.ssgdeal.order_service.domain.enums;

import lombok.Getter;

@Getter
public enum TotalOrderStatus {
    PENDING("대기"),
    PAID("주문 완료"),
    FAILED("주문 실패"),
    CANCELED("주문 취소"),
    EXPIRED("주문 만료"),
    ;

    private final String description;

    TotalOrderStatus(String description) {
        this.description = description;
    }

}
