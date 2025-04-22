package on.ssgdeal.common.messaging.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MessagingExceptionCode implements ExceptionCode {

    NON_RECOVERABLE(HttpStatus.CONFLICT, "재시도 하지 않을 예외가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
