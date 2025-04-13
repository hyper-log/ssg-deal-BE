package on.ssgdeal.cart_service.exception;

import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public enum CartExceptionCode implements ExceptionCode {

    NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "장바구니에 담을 수 있는 수량이 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    CartExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
