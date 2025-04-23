package on.ssgdeal.promotion_service.application.service.strategy;

import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.enums.StockOperation;

public interface StockStrategy {

    StockOperation getOperationType();

    void apply(Product product, ProductOption option, Long amount);

}
