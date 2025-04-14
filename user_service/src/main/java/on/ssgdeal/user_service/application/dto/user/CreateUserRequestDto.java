package on.ssgdeal.user_service.application.dto.user;


public record CreateUserRequestDto(
    String nickname,
    String slackEmail
) {

}
