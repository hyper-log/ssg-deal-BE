package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.domain.entity.User;

public record UpdateUserResponse(
    String nickname,
    String slackEmail
) {

    public static UpdateUserResponse from(User user) {
        return new UpdateUserResponse(
            user.getNickname(),
            user.getSlackEmail()
        );
    }
}
