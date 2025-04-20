package on.ssgdeal.order_service.application.dto;

import java.util.List;

public record CreateOrderRequestDto(
    Long destinationId,
    String deliveryRequest,
    List<CreateSubOrderRequestDto> subOrders
) {

    public record CreateSubOrderRequestDto(List<OrderedProductDto> orderedProducts) {

        public record OrderedProductDto(Long productId, Long optionId, Long quantity) {

        }
    }

}
