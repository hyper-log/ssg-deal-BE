package on.ssgdeal.auth_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;
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
public class SlackEmail {

    // 간단한 이메일 형식 검증 정규식 (필요시 복잡한 정규식으로 변경 가능)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Column(name = "slack_email", nullable = false)
    private String email;

    public SlackEmail(final String email) {
        validate(email);
        this.email = email;
    }

    private void validate(final String email) {
        if (Objects.isNull(email) || email.isBlank()) {
            throw new AuthException(AuthExceptionCode.AUTH_SLACK_EMAIL_IS_NULL);
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new AuthException(AuthExceptionCode.AUTH_SLACK_EMAIL_INVALID_FORMAT);
        }
    }

    @Override
    public String toString() {
        return email;
    }
}