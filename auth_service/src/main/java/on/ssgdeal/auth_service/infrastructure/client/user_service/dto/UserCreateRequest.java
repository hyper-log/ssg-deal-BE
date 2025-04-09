package on.ssgdeal.auth_service.infrastructure.client.user_service.dto;


import on.ssgdeal.auth_service.application.service.dto.SignupAuthRequestDto;

public record UserCreateRequest(
    String nickname,
    String slackEmail
) {

    public static UserCreateRequest from(SignupAuthRequestDto dto) {
        return new UserCreateRequest(
            dto.nickname(),
            dto.slackEmail()
        );
    }

}
