package on.ssgdeal.user_service.presentation.internal.dto;


import on.ssgdeal.user_service.domain.entity.User;

public record FindByIdUserResponse(
    Long userId,
    String nickname,
    String slackEmail
) {

    public static FindByIdUserResponse from(User user) {
        return new FindByIdUserResponse(
            user.getId(),
            user.getNickname(),
            user.getSlackEmail()
        );
    }

}
