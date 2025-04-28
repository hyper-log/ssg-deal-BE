package on.ssgdeal.promotion_service.application.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.application.service.dto.stock.UpdateStockRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.stock.UpdateStockResponseDto;
import on.ssgdeal.promotion_service.application.service.strategy.StockStrategy;
import on.ssgdeal.promotion_service.domain.entity.Product;
import on.ssgdeal.promotion_service.domain.entity.ProductOption;
import on.ssgdeal.promotion_service.domain.enums.StockOperation;
import on.ssgdeal.promotion_service.domain.repository.ProductRepository;
import on.ssgdeal.promotion_service.exception.ProductException.ProductNotFoundException;
import on.ssgdeal.promotion_service.exception.ProductException.ProductOptionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;
    private final List<StockStrategy> strategies;
    private Map<StockOperation, StockStrategy> strategyMap;

    @PostConstruct
    public void init() {
        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(StockStrategy::getOperationType, Function.identity()));
    }

    @Transactional
    public UpdateStockResponseDto updateStock(StockOperation operation, UpdateStockRequestDto dto) {
        Product product = productRepository.findWithOptionsById(dto.productId())
            .orElseThrow(ProductNotFoundException::new);

        ProductOption option = product.getOptions().stream()
            .filter(o -> o.getId().equals(dto.optionId()))
            .findFirst()
            .orElseThrow(ProductOptionNotFoundException::new);

        strategyMap.get(operation).apply(product, option, dto.amount());

        return UpdateStockResponseDto.from(product, option);
    }

}
