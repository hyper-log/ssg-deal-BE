package on.ssgdeal.common.mdc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MdcKey {

    USER_ID("userID"),
    PASSPORT_ID("passportId"),
    USERNAME("username"),
    ROLE("role"),
    NICKNAME("nickname"),
    SLACK_EMAIL("slackEmail"),
    ;

    private final String key;
}
