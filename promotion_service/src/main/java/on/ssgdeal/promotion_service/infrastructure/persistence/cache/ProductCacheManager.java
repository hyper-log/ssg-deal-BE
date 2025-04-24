package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCacheManager {

    private final ProductCacheService cacheService;

    public ProductCacheManager(ProductCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void evict(Long productId) {
        cacheService.evictProductCache(productId);
    }

    public void save(CachingProductDto dto) {
        cacheService.saveProductCache(dto);
    }
    public void save(List<CachingProductDto> dtos) {
        cacheService.saveProductListCache(dtos);
    }
}
