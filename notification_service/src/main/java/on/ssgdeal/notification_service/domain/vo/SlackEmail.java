package on.ssgdeal.notification_service.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class SlackEmail {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private String value;

    public SlackEmail(final String value) {
        validate(value);
        this.value = value;
    }

    public static void validate(final String value) {
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("이메일 형식이 맞지 않습니다.");
        }
    }

}