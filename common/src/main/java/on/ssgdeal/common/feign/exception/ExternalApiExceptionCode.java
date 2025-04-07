package on.ssgdeal.common.feign.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExternalApiExceptionCode implements ExceptionCode {

    CLIENT_ERROR_RESPONSE(HttpStatus.BAD_REQUEST, "요청한 API에서 클라이언트 에러가 발생했습니다."),
    SERVER_ERROR_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "요청한 API에서 서버 에러가 발생했습니다."),
    DEFAULT(HttpStatus.INTERNAL_SERVER_ERROR, "요청한 API에서 에러가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
