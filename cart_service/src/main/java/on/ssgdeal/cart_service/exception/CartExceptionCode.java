package on.ssgdeal.cart_service.exception;

import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public enum CartExceptionCode implements ExceptionCode {

    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 상품을 찾을 수 없습니다."),
    NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "장바구니에 담을 수 있는 수량이 아닙니다."),
    MUST_BE_POSITIVE_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 0보다 커야 합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    CartExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
