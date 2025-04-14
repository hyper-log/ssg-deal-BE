package on.ssgdeal.order_service.infrastructure.client.cart.feign.dtos;

import java.util.List;
import on.ssgdeal.order_service.domain.entity.TotalOrder;

public record ClearCartRequestDto(List<ClearProductInfoDto> productList) {

    public static ClearCartRequestDto from(TotalOrder totalOrder) {

        List<ClearProductInfoDto> productList = totalOrder.getOrders().stream()
            .flatMap(
                order -> order.getOrderProducts().stream()
                    .map(
                        product -> new ClearProductInfoDto(
                            product.getProductId(),
                            product.getOptionId()
                        )
                    )
            )
            .toList();

        return new ClearCartRequestDto(productList);
    }

    public record ClearProductInfoDto(Long productId, Long optionId) {

    }

}
