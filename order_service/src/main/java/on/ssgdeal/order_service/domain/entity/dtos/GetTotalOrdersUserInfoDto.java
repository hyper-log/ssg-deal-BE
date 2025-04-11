package on.ssgdeal.order_service.domain.entity.dtos;

import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;

public record GetTotalOrdersUserInfoDto(Long userId, String username, String nickname,
                                        String slackEmail) {

    public static LoginUserInfoDto from(LoginUserInfoDto dto) {
        return new LoginUserInfoDto(dto.userId(), dto.username(), dto.nickname(), dto.slackEmail());
    }
}