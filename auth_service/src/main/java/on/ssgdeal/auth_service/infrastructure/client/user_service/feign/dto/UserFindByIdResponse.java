package on.ssgdeal.auth_service.infrastructure.client.user_service.feign.dto;


public record UserFindByIdResponse(
    Long userId,
    String nickname,
    String slackEmail
) {


}
