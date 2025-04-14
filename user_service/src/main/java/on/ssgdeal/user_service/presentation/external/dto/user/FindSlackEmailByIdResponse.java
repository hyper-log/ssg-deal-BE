package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.domain.entity.User;

public record FindSlackEmailByIdResponse(
    String slackEmail
) {

    public static FindSlackEmailByIdResponse from(User user) {
        return new FindSlackEmailByIdResponse(
            user.getSlackEmail()
        );
    }
}
