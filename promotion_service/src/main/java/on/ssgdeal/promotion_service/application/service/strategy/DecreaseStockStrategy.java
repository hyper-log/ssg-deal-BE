package on.ssgdeal.promotion_service.application.service.strategy;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.enums.StockOperation;
import on.ssgdeal.promotion_service.exception.ProductException.ProductDoNotExistException;
import on.ssgdeal.promotion_service.exception.ProductException.ProductPromotionIsNotInProgressException;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.ProductCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DecreaseStockStrategy implements StockStrategy {

    private final RedisTemplate<String, String> redisTemplate;
    private String decreaseStockScript;

    @PostConstruct
    public void init() {
        this.decreaseStockScript = """
            local currentStock = tonumber(redis.call('GET', KEYS[1]) or "0")
            local decreaseAmount = tonumber(ARGV[1])
            
            if currentStock < decreaseAmount then
                return "err:INSUFFICIENT_STOCK"
            end
            
            local newStock = currentStock - decreaseAmount
            redis.call('SET', KEYS[1], newStock)
            
            return "ok:" .. newStock
            """;
    }

    @Override
    public StockOperation getOperationType() {
        return StockOperation.DECREASE;
    }

    @Override
    public void apply(Product product, ProductOption option, Long amount) {
        if (!product.getCompany().getPromotion().getStatus().equals(PromotionStatus.IN_PROGRESS)) {
            throw new ProductPromotionIsNotInProgressException();
        }

        String stockKey = ProductCacheService.PRODUCT_STOCK_KEY_PATTERN.formatted(
            product.getId(),
            option.getId());

        initializeStockIfNeeded(stockKey, option.getProductStock().getValue());

        Object result = redisTemplate.execute(
            new DefaultRedisScript<>(decreaseStockScript, Object.class),
            Collections.singletonList(stockKey),
            amount.toString()
        );

        if (result instanceof String str) {
            if (str.startsWith("err:")) {
                String error = str.substring(4);
                if ("INSUFFICIENT_STOCK".equals(error)) {
                    throw new ProductDoNotExistException();
                } else {
                    throw new RuntimeException("Unknown error: " + error);
                }
            } else if (str.startsWith("ok:")) {
                option.getProductStock().decrease(amount);
            }
        } else {
            throw new RuntimeException("Unexpected response from Redis: " + result);
        }
    }

    private void initializeStockIfNeeded(String key, Long initialStock) {
        Boolean keyExists = redisTemplate.hasKey(key);
        if (Boolean.FALSE.equals(keyExists)) {
            redisTemplate.opsForValue().set(key, initialStock.toString());
        }
    }

}
