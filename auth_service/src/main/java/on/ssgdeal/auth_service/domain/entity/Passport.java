package on.ssgdeal.auth_service.domain.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto.UserFindByIdResponse;
import on.ssgdeal.common.auth.enums.AuthRole;


@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Passport {

    private Long userId;
    private String username;
    private AuthRole role;
    private String nickname;
    private String slackEmail;

    public static Passport from(UserFindByIdResponse dto, String username, String role) {
        return Passport.builder()
            .userId(dto.userId())
            .username(username)
            .role(AuthRole.valueOf(role))
            .nickname(dto.nickname())
            .slackEmail(dto.slackEmail())
            .build();
    }
}
