package on.ssgdeal.cart_service.infrastructure.persistence.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteCartProducts(String key, List<String> hashKeys) {
        hashKeys.forEach(hashKey -> redisTemplate.opsForHash().delete(key, hashKey));
    }
}
