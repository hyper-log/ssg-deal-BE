package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockResponseDto;

public interface PromotionService {

    GetProductInfoDto getProductInfoAndStockDecrease(
        GetProductInfoRequestDto dto);

    InCreaseProductStockResponseDto increaseProductStock(
        InCreaseProductStockRequestDto dto
    );

    DecreaseProductStockResponseDto decreaseProductStock(
        DecreaseProductStockRequestDto dto
    );
}
