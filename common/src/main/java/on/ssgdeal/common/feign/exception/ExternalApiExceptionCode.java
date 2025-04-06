package on.ssgdeal.common.feign.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExternalApiExceptionCode implements ExceptionCode {

    NOT_FOUND_RESPONSE(HttpStatus.NOT_FOUND, "요청한 API에서 데이터를 찾을 수 없습니다."),
    BAD_REQUEST_RESPONSE(HttpStatus.BAD_REQUEST, "요청한 API에 잘못된 요청을 보냈습니다."),
    CLIENT_ERROR_RESPONSE(HttpStatus.BAD_REQUEST, "요청한 API에서 클라이언트 에러가 발생했습니다."),
    SERVER_ERROR_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "요청한 API에서 서버 에러가 발생했습니다."),
    WRONG_RESPONSE_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "요청한 API의 응답 타입이 잘못 설정되었습니다."),
    DEFAULT(HttpStatus.INTERNAL_SERVER_ERROR, "요청한 API에서 에러가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
