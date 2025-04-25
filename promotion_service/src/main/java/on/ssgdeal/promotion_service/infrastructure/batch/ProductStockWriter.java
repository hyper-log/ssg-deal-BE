package on.ssgdeal.promotion_service.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.ProductCacheManager;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductStockWriter implements ItemWriter<List<Product>> {

    private final ProductCacheManager productCacheManager;

    @Override
    public void write(Chunk<? extends List<Product>> chunk) throws Exception {
        for (List<Product> products : chunk) {
            productCacheManager.saveStock(products);
        }
    }
}
