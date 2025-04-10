package on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto;

public record UserCreateResponse(
    Long userId,
    String nickname,
    String slackEmail
) {

}
