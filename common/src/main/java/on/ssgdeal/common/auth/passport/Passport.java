package on.ssgdeal.common.auth.passport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import on.ssgdeal.common.auth.enums.AuthRole;

@Getter
@NoArgsConstructor
public class Passport {

    private Long userId;
    private String username;
    private AuthRole role;
    private String nickname;
    private String slackEmail;

}