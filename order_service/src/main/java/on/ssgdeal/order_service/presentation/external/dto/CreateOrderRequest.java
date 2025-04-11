package on.ssgdeal.order_service.presentation.external.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto.CreateSubOrderRequestDto;

public record CreateOrderRequest(
    @NotNull(message = "배송지는 필수 입력 값입니다.") Long destinationId,
    String deliveryRequest,
    @Valid @NotEmpty(message = "주문 상품은 필수 입력 값입니다.") List<CreateSubOrderRequest> subOrders
) {

    public CreateOrderRequestDto toDto() {
        return new CreateOrderRequestDto(
            destinationId(),
            deliveryRequest(),
            subOrders().stream()
                .map(sub -> new CreateSubOrderRequestDto(
                    sub.orderedProducts().stream()
                        .map(p -> new CreateSubOrderRequestDto.OrderedProductDto(
                            p.productId(),
                            p.optionId(),
                            p.quantity()
                        ))
                        .toList()
                ))
                .toList());
    }

    public record CreateSubOrderRequest(List<OrderedProduct> orderedProducts) {

        public record OrderedProduct(
            @NotNull(message = "상품 ID는 필수 입력 값입니다.") Long productId,
            @NotNull(message = "옵션 ID는 필수 입력 값입니다.") Long optionId,
            @NotNull(message = "상품 수량은 필수 입력 값입니다.") Long quantity
        ) {

        }
    }

}
