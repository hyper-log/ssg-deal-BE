package on.ssgdeal.order_service.application.dto;

public record CancelOrderRequestDto(Long totalOrderId,
                                    Long orderId,
                                    LoginUserInfoDto loginUserInfo
) {

    public static CancelOrderRequestDto from(
        Long totalOrderId,
        Long orderId,
        LoginUserInfoDto loginUserInfo
    ) {
        return new CancelOrderRequestDto(totalOrderId, orderId, loginUserInfo);
    }
}
