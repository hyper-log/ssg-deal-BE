package on.ssgdeal.common.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {

    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 매개변수입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 문제가 발생되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
