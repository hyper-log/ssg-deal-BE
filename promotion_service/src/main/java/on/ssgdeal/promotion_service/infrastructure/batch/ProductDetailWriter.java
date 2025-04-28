package on.ssgdeal.promotion_service.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.ProductCacheManager;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDetailWriter implements ItemWriter<List<CachingProductDto>> {

    private final ProductCacheManager productCacheManager;

    @Override
    public void write(Chunk<? extends List<CachingProductDto>> chunk) throws Exception {
        for (List<CachingProductDto> products : chunk) {
            log.info("프로모션 상품 상세 데이터 캐싱");
            productCacheManager.save(products);
        }
    }
}
