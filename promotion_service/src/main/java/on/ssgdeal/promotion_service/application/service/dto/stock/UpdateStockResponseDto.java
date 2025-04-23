package on.ssgdeal.promotion_service.application.service.dto.stock;

import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;

public record UpdateStockResponseDto(
    Product product,
    ProductOption productOption
) {

    public static UpdateStockResponseDto from(
        Product product,
        ProductOption productOption
    ) {
        return new UpdateStockResponseDto(
            product,
            productOption
        );
    }

}
