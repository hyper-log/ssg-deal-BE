package on.ssgdeal.order_service.application.command.support;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.PromotionService;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockManager {

    private final PromotionService promotionService;

    public void decreaseStock(GetProductInfoDto productInfo) {
        List<DecreaseProductStockRequestDto> decreaseRequest = toDecreaseRequest(productInfo);
        List<DecreaseProductStockResponseDto> decreaseSuccessList = new ArrayList<>();
        try {
            for (DecreaseProductStockRequestDto decreaseRequestDto : decreaseRequest) {
                log.info("재고 감소 요청 productId: {}", decreaseRequestDto.productId());
                DecreaseProductStockResponseDto decreaseProductStockResponseDto = promotionService.decreaseProductStock(
                    decreaseRequestDto);
                decreaseSuccessList.add(decreaseProductStockResponseDto);
                log.info("재고 감소 요청 성공 productId: {}", decreaseProductStockResponseDto.productId());
            }
        } catch (Exception e) {
            log.info("재고 감소 요청 실패, 롤백 요청 : {}", e.getMessage());
            List<InCreaseProductStockRequestDto> increaseRequest = toIncreaseRequest(
                decreaseSuccessList);
            for (InCreaseProductStockRequestDto increaseRequestDto : increaseRequest) {
                log.info("롤백 요청 productId: {}", increaseRequestDto.productId());
                promotionService.increaseProductStock(increaseRequestDto);
            }
        }
    }

    protected List<DecreaseProductStockRequestDto> toDecreaseRequest(
        GetProductInfoDto getProductInfoDto) {
        return getProductInfoDto.companyList().stream()
            .flatMap(company -> company.companyProductList().stream()).map(
                product -> DecreaseProductStockRequestDto.from(product.productId(),
                    product.optionId(), product.decreaseStockAmount())).toList();

    }

    protected List<InCreaseProductStockRequestDto> toIncreaseRequest(
        List<DecreaseProductStockResponseDto> decreaseSuccessList) {
        return decreaseSuccessList.stream().map(
            product -> InCreaseProductStockRequestDto.from(product.productId(), product.optionId(),
                product.decreaseStockAmount())).toList();
    }
}
