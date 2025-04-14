package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.domain.entity.User;

public record SearchUserResponse(
    Long userId,
    String nickname,
    String slackEmail
) {

    public static SearchUserResponse from(User user) {
        return new SearchUserResponse(
            user.getId(),
            user.getNickname(),
            user.getSlackEmail()
        );
    }
}
