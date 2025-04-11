package on.ssgdeal.payment_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionCode implements ExceptionCode {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역을 찾을 수 없습니다."),
    PAYMENT_CONFIRM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 승인 요청에서 서버 에러가 발생했습니다. 관리자에게 문의해주세요."),
    PAYMENT_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "결제 승인 요청 중 Timeout이 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
