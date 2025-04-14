package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.application.dto.user.CreateUserRequestDto;

public record CreateUserRequest(
    String nickname,
    String slackEmail
) {

    public CreateUserRequestDto toDto() {
        return new CreateUserRequestDto(
            nickname,
            slackEmail
        );
    }
}
