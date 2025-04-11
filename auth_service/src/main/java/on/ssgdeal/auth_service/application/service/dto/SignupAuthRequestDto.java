package on.ssgdeal.auth_service.application.service.dto;


import on.ssgdeal.auth_service.domain.vo.Password;
import on.ssgdeal.auth_service.domain.vo.Username;
import on.ssgdeal.auth_service.presentation.external.dto.SignupAuthRequest;
import on.ssgdeal.common.auth.enums.AuthRole;

public record SignupAuthRequestDto(
    Username username,
    Password password,
    String nickname,
    AuthRole role,
    String slackEmail
) {

    public static SignupAuthRequestDto from(SignupAuthRequest request) {
        return new SignupAuthRequestDto(
            new Username(request.username()),
            new Password(request.password()),
            request.nickname(),
            request.role(),
            request.slackEmail()
        );
    }
}
