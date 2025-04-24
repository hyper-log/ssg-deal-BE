package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.exception.ProductException.ProductCacheSerializeFailedException;
import on.ssgdeal.promotion_service.exception.ProductException.ProductNotFoundException;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private static final String PRODUCT_KEY_PATTERN = "promotion:product:%d:v%d";
    private static final String PRODUCT_KEYS_SCAN_PATTERN = "promotion:product:%d:v*";
    private static final Duration CACHE_MARGIN = Duration.ofMinutes(10);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public void saveProductListCache(List<CachingProductDto> dtos) {
        for (CachingProductDto dto : dtos) {
            saveProductCache(dto);
        }
    }

    public void saveProductCache(CachingProductDto dto) {
        try {
            Long ttl = getTtl(dto.getProductId());
            if (ttl == 0L) {
                return;
            }
            String json = objectMapper.writeValueAsString(dto);
            String key = String.format(PRODUCT_KEY_PATTERN, dto.getProductId(), dto.getVersion());
            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttl));
        } catch (JsonProcessingException e) {
            throw new ProductCacheSerializeFailedException();
        }
    }

    public void evictProductCache(Long productId) {
        String pattern = String.format(PRODUCT_KEYS_SCAN_PATTERN, productId);
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public Long getTtl(Long productId) {
        Product product = productRepository.findWithPromotionById(productId)
            .orElseThrow(ProductNotFoundException::new);
        LocalDate today = LocalDate.now();
        LocalDate endDate = product
            .getCompany()
            .getPromotion()
            .getEndPromotionDate();

        long days = ChronoUnit.DAYS.between(today, endDate);
        if (days <= 0) {
            return 0L;
        }
        return Duration.ofDays(days).plus(CACHE_MARGIN).getSeconds();
    }

}
