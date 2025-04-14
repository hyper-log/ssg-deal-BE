package on.ssgdeal.order_service.presentation.internal.dto;

import jakarta.validation.constraints.NotNull;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderFailRequestDto;

public record UpdateTotalOrderFailRequest(@NotNull Long totalOrderId) {

    public UpdateTotalOrderFailRequestDto toDto() {
        return new UpdateTotalOrderFailRequestDto(totalOrderId);
    }
}