package on.ssgdeal.order_service.application.command.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCommandSupport {

    private final DestinationValidator destinationValidator;
    private final ProductValidator productValidator;
    private final StockManager stockManager;

    public ValidDestinationResponseDto validateDestination(Long destinationId) {
        return destinationValidator.valid(destinationId);
    }

    public GetProductInfoDto validateProductInfo(CreateOrderRequestDto request) {
        return productValidator.valid(request);
    }

    public void decreaseStock(GetProductInfoDto productInfo) {
        stockManager.decreaseStock(productInfo);
    }

}
