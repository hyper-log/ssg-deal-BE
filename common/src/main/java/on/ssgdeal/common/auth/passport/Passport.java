package on.ssgdeal.common.auth.passport;

import lombok.Getter;

@Getter
public class Passport {

    private Long userId;
    private String username;
    private String role;
    private String nickname;
    private String slackEmail;

}
