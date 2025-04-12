package on.ssgdeal.user_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.user_service.exception.UserException;
import on.ssgdeal.user_service.exception.UserExceptionCode;

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

    public static SlackEmail valueOf(String email) {
        return new SlackEmail(email);
    }

    private void validate(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserException(UserExceptionCode.USER_SLACK_EMAIL_INVALID_FORMAT);
        }
    }

    @Override
    public String toString() {
        return email;
    }
}