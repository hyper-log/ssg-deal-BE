package on.ssgdeal.cart_service.infrastructure.persistence.repository.dto;

import on.ssgdeal.cart_service.domain.entity.CartProduct;

public record UpdateCartProductDto(
    String key,
    CartProduct cartProduct
) {

    public static UpdateCartProductDto from(String key, CartProduct exist) {
        return new UpdateCartProductDto(key, exist);
    }
}
