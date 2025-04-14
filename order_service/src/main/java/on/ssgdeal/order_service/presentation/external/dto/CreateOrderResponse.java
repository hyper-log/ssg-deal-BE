package on.ssgdeal.order_service.presentation.external.dto;

import on.ssgdeal.order_service.domain.entity.TotalOrder;

public record CreateOrderResponse(Long orderId, Long price) {

    public static CreateOrderResponse from(TotalOrder totalOrder) {
        return new CreateOrderResponse(totalOrder.getId(), totalOrder.getPrice().getValue());
    }

}

