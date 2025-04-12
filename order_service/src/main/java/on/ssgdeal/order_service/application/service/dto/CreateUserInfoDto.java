package on.ssgdeal.order_service.application.service.dto;

import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;

public record CreateUserInfoDto(
    Long userId,
    String nickname,
    String slackEmail,
    String destination,
    String deliveryRequest
) {

    public static CreateUserInfoDto from(
        CreateOrderRequestDto createOrderRequestDto,
        LoginUserInfoDto loginUserInfoDto,
        ValidDestinationResponseDto validDestinationResponseDto
    ) {
        return new CreateUserInfoDto(
            loginUserInfoDto.userId(),
            loginUserInfoDto.nickname(),
            loginUserInfoDto.slackEmail(),
            validDestinationResponseDto.address(),
            createOrderRequestDto.deliveryRequest());
    }

}