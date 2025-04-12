package on.ssgdeal.order_service.presentation.internal.dto;

import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderFailRequestDto;

public record UpdateTotalOrderFailRequest(Long totalOrderId) {

    public UpdateTotalOrderFailRequestDto toDto() {
        return new UpdateTotalOrderFailRequestDto(totalOrderId);
    }
}