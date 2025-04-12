package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.domain.entity.User;

public record UpdateUserAdminResponse(
    String nickname,
    String slackEmail
) {

    public static UpdateUserAdminResponse from(User user) {
        return new UpdateUserAdminResponse(
            user.getNickname(),
            user.getSlackEmail()
        );
    }
}
