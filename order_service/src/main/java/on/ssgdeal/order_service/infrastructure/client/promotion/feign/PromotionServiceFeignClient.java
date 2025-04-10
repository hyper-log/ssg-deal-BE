package on.ssgdeal.order_service.infrastructure.client.promotion.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.InCreaseProductStockResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "promotion-service")
public interface PromotionServiceFeignClient {

    @PostMapping("/internal/v1/promotions/products/stocks/decrease/detail")
    CommonResponse<GetProductInfoDto> getProductInfoAndStockDecrease(
        @RequestBody GetProductInfoRequestDto getProductInfoRequestDto);

    @PostMapping("/internal/v1/promotions/products/stocks/increase")
    CommonResponse<InCreaseProductStockResponseDto> increaseStock(
        @RequestBody InCreaseProductStockRequestDto increaseProductStockRequestDto
    );

    @PostMapping("/internal/v1/promotions/products/stocks/decrease")
    CommonResponse<DecreaseProductStockResponseDto> decreaseStock(
        @RequestBody DecreaseProductStockRequestDto decreaseProductStockRequestDto
    );
}