package on.ssgdeal.promotion_service.infrastructure.batch;

import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.dto.CachingProductDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDetailToDtoProcessor implements ItemProcessor<List<Product>, List<CachingProductDto>> {

    @Override
    public List<CachingProductDto> process(List<Product> products) throws Exception {
        return products.stream()
                .map(CachingProductDto::from)
                .collect(Collectors.toList());
    }
}