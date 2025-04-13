package on.ssgdeal.cart_service.application.service;

import static on.ssgdeal.cart_service.exception.CartException.CartProductNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.dto.UpdateCartProductRequestDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.UpdateCartProductDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "cart-service")
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public void updateCartProduct(UpdateCartProductRequestDto requestDto) {
        log.info("update cart product: {}", requestDto);

        String key = RedisKeyGenerator.generateKey(requestDto.userId());
        String hashKey = RedisKeyGenerator.generateHashKey(
            requestDto.productId(), requestDto.optionId());

        cartRepository.findCartProduct(key, hashKey)
            .ifPresentOrElse(
                exist -> {
                    if (requestDto.afterOptionId() != null) {
                        String updateHashKey = RedisKeyGenerator.generateHashKey(
                            requestDto.productId(), requestDto.afterOptionId());
                        CartProduct updateCartProduct;
                        if (requestDto.afterQuantity() != null) {
                            updateCartProduct = CartProduct.create(
                                updateHashKey, requestDto.afterQuantity());
                        } else {
                            updateCartProduct = CartProduct.create(
                                updateHashKey, requestDto.quantity());
                        }
                        cartRepository.updateCartProduct(
                            UpdateCartProductDto.from(key, updateCartProduct));
                    } else {
                        if (requestDto.afterQuantity() != null) {
                            CartProduct updateCartProduct = CartProduct.create(
                                hashKey, requestDto.afterQuantity());
                            cartRepository.updateCartProduct(
                                UpdateCartProductDto.from(key, updateCartProduct));
                        }
                    }
                },
                () -> {
                    log.warn("cart product not found: {}", requestDto);
                    throw new CartProductNotFoundException();
                }
            );
    }
}
