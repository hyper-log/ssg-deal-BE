package on.ssgdeal.user_service.presentation.internal.dto;

import on.ssgdeal.user_service.domain.entity.User;

public record FindMyUserResponse(
    Long userId,
    String nickname,
    String slackEmail
) {

    public static FindMyUserResponse from(User user) {
        return new FindMyUserResponse(
            user.getId(),
            user.getNickname(),
            user.getSlackEmail()
        );
    }

}

