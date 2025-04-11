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
public class Username {

    public static final int MIN_LENGTH = 4;
    public static final int MAX_LENGTH = 10;

    @Column(length = MAX_LENGTH, name = "username", nullable = false)
    private String username;

    public Username(final String username) {
        validate(username);
        this.username = username;
    }

    private void validate(final String username) {
        if (Objects.isNull(username)) {
            throw new AuthException(AuthExceptionCode.AUTH_USERNAME_IS_NULL);
        }
        if (username.length() < MIN_LENGTH) {
            throw new AuthException(AuthExceptionCode.AUTH_USERNAME_MIN_LENGTH);
        }
        if (username.length() > MAX_LENGTH) {
            throw new AuthException(AuthExceptionCode.AUTH_USERNAME_MAX_LENGTH);
        }
        if (!username.matches("^[a-z0-9]+$")) {
            throw new AuthException(AuthExceptionCode.AUTH_USERNAME_INVALID_FORMAT);
        }
    }

    @Override
    public String toString() {
        return username;
    }
}
