package on.ssgdeal.cart_service.domain.repository;

import java.util.Optional;
import java.util.List;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;

public interface CartRepository {

    void deleteCartProducts(String key, List<String> hashKeys);

    List<CartProduct> findAll(String key);

    void addCartProduct(AddCartProductDto dto);

    Optional<CartProduct> findCartProduct(String key, String hashKey);

    void updateCartProduct(UpdateCartProductDto dto);
}
