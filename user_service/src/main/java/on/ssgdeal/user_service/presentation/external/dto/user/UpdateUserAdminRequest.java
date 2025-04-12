package on.ssgdeal.user_service.presentation.external.dto.user;

import on.ssgdeal.user_service.application.dto.user.UpdateUserAdminRequestDto;
import on.ssgdeal.user_service.domain.vo.SlackEmail;

public record UpdateUserAdminRequest(
    String nickname,
    String slackEmail
) {

    public UpdateUserAdminRequestDto toDto(Long id) {
        return new UpdateUserAdminRequestDto(
            id,
            nickname,
            new SlackEmail(slackEmail)
        );
    }

}
