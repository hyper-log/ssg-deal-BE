package on.ssgdeal.auth_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.auth_service.exception.AuthException;
import on.ssgdeal.auth_service.exception.AuthExceptionCode;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 15;

    @Column(length = MAX_LENGTH, name = "password", nullable = false)
    private String password;

    public Password(final String password) {
        validate(password);
        this.password = password;
    }

    private void validate(final String password) {
        if (Objects.isNull(password)) {
            throw new AuthException(AuthExceptionCode.AUTH_PASSWORD_IS_NULL);
        }
        if (password.length() < MIN_LENGTH) {
            throw new AuthException(AuthExceptionCode.AUTH_PASSWORD_MIN_LENGTH);
        }
        if (password.length() > MAX_LENGTH) {
            throw new AuthException(AuthExceptionCode.AUTH_PASSWORD_MAX_LENGTH);
        }
        // 최소 1개의 소문자, 1개의 대문자, 1개의 숫자, 1개의 특수문자가 포함되어야 함.
        // 특수문자 범위는 필요에 따라 조정 가능함.
        if (!password.matches(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$")) {
            throw new AuthException(AuthExceptionCode.AUTH_PASSWORD_INVALID_FORMAT);
        }
    }

    @Override
    public String toString() {
        return password;
    }
}