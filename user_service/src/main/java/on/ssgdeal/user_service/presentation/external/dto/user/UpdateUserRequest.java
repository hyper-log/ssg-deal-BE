package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.application.dto.user.UpdateUserRequestDto;
import on.ssgdeal.user_service.domain.vo.SlackEmail;

public record UpdateUserRequest(
    String nickname,
    String slackEmail
) {

    public UpdateUserRequestDto toDto() {
        return new UpdateUserRequestDto(
            nickname,
            new SlackEmail(slackEmail)
        );
    }

}
