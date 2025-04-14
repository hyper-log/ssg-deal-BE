package on.ssgdeal.user_service.application.dto.user;

import org.springframework.data.domain.Pageable;

public record SearchUserRequestDto(
    String nickname,
    String slackEmail,
    Pageable pageable
) {

    public static SearchUserRequestDto from(
        String nickname,
        String slackEmail,
        Pageable pageable
    ) {
        return new SearchUserRequestDto(
            nickname,
            slackEmail,
            pageable);
    }

}
