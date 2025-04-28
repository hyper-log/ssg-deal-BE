package on.ssgdeal.promotion_service.application.service.strategy;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.enums.StockOperation;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.ProductCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncreaseStockStrategy implements StockStrategy {

    private final RedisTemplate<String, String> redisTemplate;
    private String increaseStockScript;

    @PostConstruct
    public void init() {
        this.increaseStockScript = """
            local currentStock = tonumber(redis.call('GET', KEYS[1]) or "0")
            local increaseAmount = tonumber(ARGV[1])
            
            local newStock = currentStock + increaseAmount
            redis.call('SET', KEYS[1], newStock)
            
            return newStock
            """;
    }

    @Override
    public StockOperation getOperationType() {
        return StockOperation.INCREASE;
    }

    @Override
    public void apply(Product product, ProductOption option, Long amount) {
        String stockKey = ProductCacheService.PRODUCT_STOCK_KEY_PATTERN.formatted(
            product.getId(),
            option.getId());

        initializeStockIfNeeded(stockKey, option.getProductStock().getValue());

        Long newStock = redisTemplate.execute(
            new DefaultRedisScript<>(increaseStockScript, Long.class),
            Collections.singletonList(stockKey),
            amount.toString()
        );

        if (newStock != null) {
            option.getProductStock().increase(amount);
        } else {
            option.getProductStock().increase(amount);
        }
    }

    private void initializeStockIfNeeded(String key, Long initialStock) {
        Boolean keyExists = redisTemplate.hasKey(key);
        if (Boolean.FALSE.equals(keyExists)) {
            redisTemplate.opsForValue().set(key, initialStock.toString());
        }
    }
}
