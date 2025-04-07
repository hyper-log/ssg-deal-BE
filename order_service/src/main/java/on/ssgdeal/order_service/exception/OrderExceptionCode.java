package on.ssgdeal.order_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCode implements ExceptionCode {
    ORDER_NULL_PRICE(HttpStatus.BAD_REQUEST, "주문 총 금액은 필수 입력 값입니다."),
    ORDER_MIN_PRICE(HttpStatus.BAD_REQUEST, "주문 총 금액은 0원일 수 없습니다."),
    ORDER_NULL_TOTAL_ORDER_NUMBER(HttpStatus.BAD_REQUEST, "주문 금액은 필수 값입니다"),
    ORDER_FORMAT_TOTAL_ORDER_NUMBER(HttpStatus.BAD_REQUEST, "주문 번호 형식이 올바르지 않습니다."),
    ORDER_NULL_STATUS(HttpStatus.BAD_REQUEST, "주문 상태는 필수 입력 값입니다."),
    ORDER_MAX_DELIVERY_REQUEST(HttpStatus.BAD_REQUEST, "주문 요청 사항은 20자 이상을 넘어갈 수 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
