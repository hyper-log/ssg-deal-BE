package on.ssgdeal.cart_service.infrastructure.persistence.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisKeyGenerator {

    private static final String CART_KEY_PREFIX = "cart";
    private static final String KEY_SEPARATOR = ":";

    public static String generateKey(Long userId) {
        return CART_KEY_PREFIX + KEY_SEPARATOR + userId;
    }

    public static String generateHashKey(Long productId, Long optionId) {
        return productId + KEY_SEPARATOR + optionId;
    }
}
