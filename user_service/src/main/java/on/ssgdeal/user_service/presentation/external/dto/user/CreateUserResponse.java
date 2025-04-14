package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.domain.entity.User;

public record CreateUserResponse(
    Long userId,
    String nickname,
    String slackEmail
) {

    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(
            user.getId(),
            user.getNickname(),
            user.getSlackEmail()
        );
    }
}
