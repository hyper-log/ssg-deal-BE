package on.ssgdeal.order_service.application.service.dto;

import on.ssgdeal.order_service.domain.entity.TotalOrder;

public record CancelTotalOrderResponseDto(Long orderId) {

    public static CancelTotalOrderResponseDto from(TotalOrder totalOrder) {
        return new CancelTotalOrderResponseDto(totalOrder.getId());
    }

}
