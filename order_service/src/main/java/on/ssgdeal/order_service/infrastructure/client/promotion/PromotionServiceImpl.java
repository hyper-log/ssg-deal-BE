package on.ssgdeal.order_service.infrastructure.client.promotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.PromotionService;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.PromotionServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PromotionServiceImpl")
public class PromotionServiceImpl implements PromotionService {

    private final PromotionServiceFeignClient feignClient;

    @Override
    public GetProductInfoDto getProductInfoAndStockDecrease(GetProductInfoRequestDto requestDto) {
        log.info("getProductInfoAndStockDecrease : {}", requestDto.toString());
        var getProductInfoDto = feignClient.getProductInfoAndStockDecrease(requestDto);
        return getProductInfoDto.data();
    }

    @Override
    public InCreaseProductStockResponseDto increaseProductStock(
        InCreaseProductStockRequestDto requestDto) {
        log.info("increaseProductStock : {}", requestDto.toString());
        var inCreaseProductStockResponseDto = feignClient.increaseStock(requestDto);
        return inCreaseProductStockResponseDto.data();
    }

    @Override
    public DecreaseProductStockResponseDto decreaseProductStock(
        DecreaseProductStockRequestDto requestDto) {
        log.info("decreaseProductStock : {}", requestDto.toString());
        var decreaseProductStockResponseDto = feignClient.decreaseStock(requestDto);
        return decreaseProductStockResponseDto.data();
    }
}
