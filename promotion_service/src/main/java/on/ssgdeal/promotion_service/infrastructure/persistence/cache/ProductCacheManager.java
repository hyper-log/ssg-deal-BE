package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductCacheManager {

    private final ProductCacheService cacheService;

    public ProductCacheManager(ProductCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void evict(Long productId) {
        cacheService.evictProductCache(productId);
    }

    public void update(CachingProductDto dto) {
        cacheService.updateProductCache(dto);
    }
}
