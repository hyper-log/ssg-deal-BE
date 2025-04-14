package on.ssgdeal.cart_service.domain.repository;

import java.util.List;
import java.util.Optional;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;

public interface CartRepository {

    List<CartProduct> findAll(String key);

    void addCartProduct(AddCartProductDto dto);

    Optional<CartProduct> findCartProduct(String key, String hashKey);

    void updateCartProduct(UpdateCartProductDto dto);
}
