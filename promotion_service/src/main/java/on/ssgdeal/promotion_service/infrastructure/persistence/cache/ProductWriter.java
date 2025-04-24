package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductWriter implements ItemWriter<List<CachingProductDto>> {

    private final ProductCacheManager productCacheManager;

    @Override
    public void write(Chunk<? extends List<CachingProductDto>> chunk) throws Exception {
        for (List<CachingProductDto> products : chunk) {
            productCacheManager.save(products);
        }
    }
}
