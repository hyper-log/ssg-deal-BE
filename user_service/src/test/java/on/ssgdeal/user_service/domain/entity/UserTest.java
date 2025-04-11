package on.ssgdeal.user_service.domain.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    @DisplayName("SlackEmail 이메일 형태 실패 검증")
    void validSlackEmail_fail() throws Exception {
        String email = "test.com";
        assertThatThrownBy(() -> new SlackEmail(email)).isInstanceOf(
            UserException.UserSlackEmailInvalidFormatException.class);
    }

    @Test
    @DisplayName("SlackEmail 이메일 형태 성공 검증")
    void validSlackEmail_success() throws Exception {
        String email = "test@test.com";
        assertThatNoException().isThrownBy(() -> new SlackEmail(email));
    }

}
