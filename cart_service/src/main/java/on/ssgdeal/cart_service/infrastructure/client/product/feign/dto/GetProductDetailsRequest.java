package on.ssgdeal.cart_service.infrastructure.client.product.feign.dto;

import java.util.List;

public record GetProductDetailsRequest(
    List<ProductDetail> getProductDetails
) {

    public record ProductDetail(
        Long productId,
        Long optionId
    ) {

    }
}
