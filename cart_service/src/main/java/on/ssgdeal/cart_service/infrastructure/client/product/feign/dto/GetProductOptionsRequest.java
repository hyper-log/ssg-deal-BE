package on.ssgdeal.cart_service.infrastructure.client.product.feign.dto;

public record GetProductOptionsRequest(
    Long productId
) {

    public static GetProductOptionsRequest from(Long productId) {
        return new GetProductOptionsRequest(productId);
    }
}
