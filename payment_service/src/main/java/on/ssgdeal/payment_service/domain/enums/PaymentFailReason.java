package on.ssgdeal.payment_service.domain.enums;

public enum PaymentFailReason {
    INSUFFICIENT_BALANCE("잔액 부족"),
    CANCELLED_BY_USER("사용자 취소"),
    TIMEOUT("결제 처리 시간 초과"),
    PAYMENT_DECLINED("결제 거절"),
    ORDER_NOT_FOUND("주문 정보 없음"),
    INVALID_REQUEST("잘못된 요청"),
    INVALID_AMOUNT("잘못된 결제 금액"),
    MISSING_REQUIRED_PARAMETER("필수 파라미터 누락"),
    TIME_EXPIRATION("결제 시간 만료"),
    UNKNOWN("알 수 없는 오류"),
    ;

    final String description;

    PaymentFailReason(String description) {
        this.description = description;
    }

    public static PaymentFailReason fromErrorCode(String errorCode) {
        return switch (errorCode) {
            case "INVALID_REQUEST" -> INVALID_REQUEST;
            case "INVALID_REQUIRED_PARAM" -> MISSING_REQUIRED_PARAMETER;
            case "PAY_PROCESS_CANCELED" -> CANCELLED_BY_USER;
            case "PAY_PROCESS_ABORTED" -> PAYMENT_DECLINED;
            case "REJECT_CARD_COMPANY" -> PAYMENT_DECLINED;
            case "NOT_FOUND_PAYMENT_SESSION" -> TIME_EXPIRATION;
            default -> UNKNOWN;
        };
    }
}
