package on.ssgdeal.common.auth.passport;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import on.ssgdeal.common.auth.enums.AuthRole;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Passport {

    private Long userId;
    private String username;
    private AuthRole role;
    private String nickname;
    private String slackEmail;

    public static Passport of(
        Long userId,
        String nickname,
        String SlackEmail,
        String username,
        String role
    ) {
        return Passport.builder()
            .userId(userId)
            .username(username)
            .role(AuthRole.valueOf(role))
            .nickname(nickname)
            .slackEmail(SlackEmail)
            .build();
    }
}

