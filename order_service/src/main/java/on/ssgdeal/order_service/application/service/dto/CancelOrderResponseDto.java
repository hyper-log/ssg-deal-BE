package on.ssgdeal.order_service.application.service.dto;

public record CancelOrderResponseDto(Long orderId) {

    public static CancelOrderResponseDto from(Long orderId) {
        return new CancelOrderResponseDto(orderId);
    }
}
