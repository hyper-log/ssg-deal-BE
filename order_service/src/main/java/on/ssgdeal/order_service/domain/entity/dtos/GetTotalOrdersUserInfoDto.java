package on.ssgdeal.order_service.domain.entity.dtos;

public record GetTotalOrdersUserInfoDto(Long userId, String username, String nickname,
                                        String slackEmail) {

}