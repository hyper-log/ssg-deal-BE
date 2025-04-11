package on.ssgdeal.auth_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
    AUTH_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 인증 정보입니다."),
    AUTH_IS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 정보입니다."),
    AUTH_USERNAME_MIN_LENGTH(HttpStatus.BAD_REQUEST, "아이디는 최소 4자 이상이어야 합니다."),
    AUTH_USERNAME_MAX_LENGTH(HttpStatus.BAD_REQUEST, "아이디는 최대 10자 이하이어야 합니다."),
    AUTH_USERNAME_IS_NULL(HttpStatus.BAD_REQUEST, "아이디는 필수 입력 값입니다."),
    AUTH_USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "아이디는 알파벳 소문자와 숫자로만 구성되어야 합니다."),
    AUTH_USERNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디입니다."),
    AUTH_PASSWORD_MIN_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 최소 8자 이상이어야 합니다."),
    AUTH_PASSWORD_MAX_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 최대 15자 이하이어야 합니다."),
    AUTH_PASSWORD_IS_NULL(HttpStatus.BAD_REQUEST, "비밀번호는 필수 입력 값입니다."),
    AUTH_PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호는 알파벳 대소문자, 숫자, 특수문자가 포함되어야 합니다."),
    AUTH_SLACK_EMAIL_IS_NULL(HttpStatus.BAD_REQUEST, "Slack 이메일은 필수 입력 값입니다."),
    AUTH_SLACK_EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Slack 이메일은 이메일 형식으로 입력되어야 합니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
