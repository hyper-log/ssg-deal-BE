package on.ssgdeal.cart_service.infrastructure.persistence.repository;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.application.service.dto.UpdateCartProductDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.persistence.repository.dto.AddCartProductDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private static final int EXPIRE_DURATION_IN_DAYS = 30;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<CartProduct> findCartProduct(String key, String hashKey) {
        Long quantity = (Long) redisTemplate.opsForHash().get(key, hashKey);
        if (Objects.nonNull(quantity)) {
            CartProduct cartProduct = CartProduct.create(hashKey, quantity);
            return Optional.of(cartProduct);
        }
        return Optional.empty();
    }

    @Override
    public void addCartProduct(AddCartProductDto dto) {
        redisTemplate.opsForHash().put(dto.key(), dto.hashKey(), dto.hashValue());
        redisTemplate.expire(dto.key(), Duration.ofDays(EXPIRE_DURATION_IN_DAYS));
    }

    @Override
    public void updateCartProduct(UpdateCartProductDto dto) {
        AddCartProductDto addCartProductDto = AddCartProductDto.from(
            dto.key(),
            dto.cartProduct().getHashKey(),
            dto.cartProduct().getQuantity()
        );
        addCartProduct(addCartProductDto);
    }
}
