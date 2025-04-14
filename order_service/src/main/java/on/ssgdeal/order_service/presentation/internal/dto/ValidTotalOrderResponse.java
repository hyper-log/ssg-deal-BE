package on.ssgdeal.order_service.presentation.internal.dto;

public record ValidTotalOrderResponse(boolean totalOrderExists) {

    public static ValidTotalOrderResponse from(boolean totalOrderExists) {
        return new ValidTotalOrderResponse(totalOrderExists);
    }
}
