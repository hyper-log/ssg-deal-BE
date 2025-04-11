package on.ssgdeal.auth_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HashExceptionCode implements ExceptionCode {
    HASH_ALGORITHM_NOT_AVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "해시 알고리즘을 사용할 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
