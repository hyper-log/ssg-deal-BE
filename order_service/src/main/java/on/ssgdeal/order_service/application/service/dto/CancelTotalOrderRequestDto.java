package on.ssgdeal.order_service.application.service.dto;

public record CancelTotalOrderRequestDto(Long totalOrderId, LoginUserInfoDto loginUserInfo) {

    public static CancelTotalOrderRequestDto from(Long totalOrderId,
        LoginUserInfoDto loginUserInfo) {
        return new CancelTotalOrderRequestDto(totalOrderId, loginUserInfo);
    }
}
