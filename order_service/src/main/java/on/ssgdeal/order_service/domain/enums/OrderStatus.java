package on.ssgdeal.order_service.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("주문 대기"),
    PAID("주문 완료"),
    FAILED("주문 실패"),
    CANCELED("주문 취소"),
    EXPIRED("주문 만료"),
    DELIVERING("배송 중"),
    DELIVERED("배송 완료"),
    EXCHANGE_REQUESTED("교환 요청"),
    EXCHANGED("교환 완료"),
    REFUND_REQUESTED("환불 요청"),
    REFUNDED("환불 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
