package on.ssgdeal.auth_service.presentation.external.dto;


import on.ssgdeal.common.auth.enums.AuthRole;

public record SignupAuthRequest(
    String username,
    String password,
    String nickname,
    AuthRole role,
    String slackEmail
) {

}
