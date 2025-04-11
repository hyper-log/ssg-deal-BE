package on.ssgdeal.auth_service.presentation.external.dto;


import on.ssgdeal.auth_service.application.service.dto.SignupAuthRequestDto;
import on.ssgdeal.auth_service.domain.vo.Password;
import on.ssgdeal.auth_service.domain.vo.Username;
import on.ssgdeal.common.auth.enums.AuthRole;

public record SignupAuthRequest(
    String username,
    String password,
    String nickname,
    AuthRole role,
    String slackEmail
) {

    public SignupAuthRequestDto toDto() {
        return new SignupAuthRequestDto(
            new Username(username),
            new Password(password),
            nickname,
            role,
            slackEmail
        );
    }

}
