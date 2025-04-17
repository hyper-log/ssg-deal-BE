package on.ssgdeal.cart_service.infrastructure.client.product.feign.dto;

import java.util.List;

public record GetProductDetailsRequest(
    List<ProductDetail> productDetails
) {

    public record ProductDetail(
        Long productId,
        Long optionId
    ) {

    }

    public static GetProductDetailsRequest from(
        List<ProductDetail> productDetailList
    ) {
        return new GetProductDetailsRequest(
            productDetailList
        );
    }
}
