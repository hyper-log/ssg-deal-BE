package on.ssgdeal.promotion_service.application.service.strategy;

import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.enums.StockOperation;
import on.ssgdeal.promotion_service.exception.ProductException.ProductPromotionIsNotInProgressException;
import org.springframework.stereotype.Component;

@Component
public class DecreaseStockStrategy implements StockStrategy {

    @Override
    public StockOperation getOperationType() {
        return StockOperation.DECREASE;
    }

    @Override
    public void apply(Product product, ProductOption option, Long amount) {
        if (!product.getCompany().getPromotion().getStatus().equals(PromotionStatus.IN_PROGRESS)) {
            throw new ProductPromotionIsNotInProgressException();
        }
        
        option.getProductStock().decrease(amount);
    }

}
