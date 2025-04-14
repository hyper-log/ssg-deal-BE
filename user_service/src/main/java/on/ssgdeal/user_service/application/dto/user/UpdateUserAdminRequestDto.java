package on.ssgdeal.user_service.application.dto.user;

import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.presentation.external.dto.user.UpdateUserAdminRequest;

public record UpdateUserAdminRequestDto(
    Long userId,
    String nickname,
    SlackEmail slackEmail
) {

    public static UpdateUserAdminRequestDto from(
        Long userId,
        UpdateUserAdminRequest updateUserAdminRequest
    ) {
        return new UpdateUserAdminRequestDto(
            userId,
            updateUserAdminRequest.nickname(),
            new SlackEmail(updateUserAdminRequest.slackEmail())
        );
    }
}
