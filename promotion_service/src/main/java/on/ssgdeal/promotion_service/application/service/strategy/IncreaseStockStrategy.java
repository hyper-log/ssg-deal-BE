package on.ssgdeal.promotion_service.application.service.strategy;

import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.enums.StockOperation;
import org.springframework.stereotype.Component;

@Component
public class IncreaseStockStrategy implements StockStrategy {

    @Override
    public StockOperation getOperationType() {
        return StockOperation.INCREASE;
    }

    @Override
    public void apply(Product product, ProductOption option, Long amount) {
        option.getProductStock().increase(amount);
    }
}
