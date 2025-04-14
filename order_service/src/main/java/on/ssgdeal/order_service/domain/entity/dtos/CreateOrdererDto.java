package on.ssgdeal.order_service.domain.entity.dtos;

import on.ssgdeal.order_service.application.service.dto.CreateUserInfoDto;

public record CreateOrdererDto(Long userId, String nickname, String slackEmail,
                               String destination, String deliveryRequest) {

    public static CreateOrdererDto from(CreateUserInfoDto request) {
        return new CreateOrdererDto(request.userId(), request.nickname(), request.slackEmail(),
            request.destination(), request.deliveryRequest());
    }
}
