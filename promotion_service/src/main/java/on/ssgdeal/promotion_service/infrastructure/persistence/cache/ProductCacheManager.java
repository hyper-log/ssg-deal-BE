package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import on.ssgdeal.promotion_service.domain.entity.Product;
import java.util.List;
import java.util.Optional;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.data.domain.Pageable;
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

    public void save(CachingProductDto dto) {
        cacheService.saveProductCache(dto);
    }

    public void save(List<CachingProductDto> dtos) {
        cacheService.saveProductListCache(dtos);
    }

    public <T> Optional<T> getView(Long productId, Class<T> viewType) {
        return cacheService.getView(productId, viewType);
    }

    public <T> T getOrLoadView(Long productId, Class<T> viewType) {
        return cacheService.getOrLoadView(productId, viewType);
    }

    public <T> SliceDto<T> getAllByPromotionOrLoad(Long promotionId,
        Pageable pageable, Class<T> viewType) {
        return cacheService.getAllByPromotionOrLoad(promotionId, pageable, viewType);
    }
    public void saveStock(List<Product> products) {
        cacheService.saveProductStockListCache(products);
    }
}
