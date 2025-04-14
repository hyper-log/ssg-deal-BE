package on.ssgdeal.user_service.application.dto.user;

import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserRequest;

public record UpdateUserRequestDto(
    String nickname,
    SlackEmail slackEmail
) {

    public static UpdateUserRequestDto from(UpdateUserRequest request) {
        return new UpdateUserRequestDto(
            request.nickname(),
            new SlackEmail(request.slackEmail())
        );
    }
}
