package on.ssgdeal.order_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCode implements ExceptionCode {
    ORDER_CREATE_EXCEPTION(HttpStatus.BAD_REQUEST, "주문 생성 중 오류가 발생했습니다."),
    ORDER_NULL_PRICE(HttpStatus.BAD_REQUEST, "주문 총 금액은 필수 입력 값입니다."),
    ORDER_NULL_TOTAL_ORDER_NUMBER(HttpStatus.BAD_REQUEST, "주문 금액은 필수 값입니다"),
    ORDER_FORMAT_TOTAL_ORDER_NUMBER(HttpStatus.BAD_REQUEST, "주문 번호 형식이 올바르지 않습니다."),
    ORDER_NULL_STATUS(HttpStatus.BAD_REQUEST, "주문 상태는 필수 입력 값입니다."),
    ORDER_MAX_DELIVERY_REQUEST(HttpStatus.BAD_REQUEST, "주문 요청 사항은 20자 이상을 넘어갈 수 없습니다."),
    ORDER_PROMOTION_FINISHED(HttpStatus.BAD_REQUEST, "프로모션이 종료된 상품이 포함되어 있어 주문을 할 수 없습니다."),
    ORDER_PROMOTION_STOCK_OVER(HttpStatus.BAD_REQUEST, "프로모션 상품의 재고가 소진되어 주문할 수 없습니다."),
    ORDER_PROMOTION_STOCK_ERROR(HttpStatus.BAD_REQUEST, "프로모션 재고 감소가 불가능합니다."),
    ORDER_VALID_DESTINATION(HttpStatus.BAD_REQUEST, "배송지를 다시 확인해 주세요."),
    ORDER_NOT_FOUND_TOTAL_ORDER(HttpStatus.NOT_FOUND, "총 주문 데이터를 찾을 수 없습니다."),
    ORDER_MIN_QUANTITY(HttpStatus.BAD_REQUEST, "주문 상품의 개수가 0개일 수 없습니다."),
    ORDER_NOT_ORDERER(HttpStatus.BAD_REQUEST, "로그인 유저와 주문자의 정보가 일치하지 않습니다."),
    ORDER_NOT_CANCEL(HttpStatus.BAD_REQUEST, "취소될 수 없는 주문 상태입니다."),
    ORDER_PAYMENTS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "현재 주문을 취소할 수 없습니다."),
    ORDER_NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문 데이터를 찾을 수 없습니다."),
    ORDER_ALREADY_CANCEL(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
