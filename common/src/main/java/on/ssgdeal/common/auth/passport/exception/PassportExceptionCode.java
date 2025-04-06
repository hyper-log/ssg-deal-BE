package on.ssgdeal.common.auth.passport.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PassportExceptionCode implements ExceptionCode {

    PASSPORT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Passport를 발급할 수 없습니다."),
    PASSPORT_RETRIEVAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Passport를 조회할 수 없습니다."),
    PASSPORT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 정보입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
