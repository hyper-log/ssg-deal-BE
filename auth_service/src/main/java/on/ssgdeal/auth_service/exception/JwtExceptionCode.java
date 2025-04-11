package on.ssgdeal.auth_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionCode implements ExceptionCode {
    JWT_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 토큰입니다."),
    JWT_INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    JWT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    JWT_UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    JWT_CLAIMS_IS_EMPTY(HttpStatus.BAD_REQUEST, "토큰의 인증 정보가 비어있습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
